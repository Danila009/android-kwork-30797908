@file:OptIn(ExperimentalMaterialApi::class)

package com.example.messenger.ui.screens.chatScreen.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.messenger.R
import com.example.messenger.ui.theme.primaryText

private enum class AttachFileType(
    val color:Color,
    val icon:Int,
    val text:String
) {
    Gallery(
        text = "Галерея",
        icon = R.drawable.gallery,
        color = Color(0xFF154EBE)
    ),
    Camera(
        text = "Камера",
        icon = R.drawable.camera,
        color = Color(0xFF15A5BE)
    )
}

@Composable
fun AttachFileCard(
    onImages:(List<Uri>) -> Unit,
    onImage:(Bitmap) -> Unit
) {
    var attachFileType by remember { mutableStateOf<AttachFileType?>(null) }

    when(attachFileType){
        AttachFileType.Gallery -> AttachFileGallery(
            onImages = {
                onImages(it)
                attachFileType = null
            }
        )
        AttachFileType.Camera -> AttachFileCamera {
            onImage(it)
            attachFileType = null
        }
        else -> Unit
    }

    LazyRow {
        item {
            AttachFileType.values().forEach { item ->
                Card(
                    modifier = Modifier
                        .padding(10.dp),
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    backgroundColor = item.color,
                    onClick = { attachFileType = item }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(35.dp),
                            tint = primaryText
                        )

                        Text(
                            text = item.text,
                            fontWeight = FontWeight.W900,
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("Recycle")
@Composable
private fun AttachFileGallery(
    onImages:(List<Uri>) -> Unit
){
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uri ->
        onImages(uri)
    }

    LaunchedEffect(key1 = Unit, block = {
        launcher.launch("image/*")
    })
}

@SuppressLint("Recycle")
@Composable
private fun AttachFileCamera(
    onImage:(Bitmap) -> Unit
){
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let { onImage(bitmap) }
    }

    LaunchedEffect(key1 = Unit, block = {
        launcher.launch()
    })
}