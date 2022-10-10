package com.example.messenger.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.messenger.ui.theme.tintColor

@Composable
fun Image(
    url:Any?,
    modifier: Modifier = Modifier,
    progressbarModifier: Modifier = Modifier,
    contentDescription:String? = null,
    progressbarColor:Color = tintColor
) {
    val progressIndicator = remember { mutableStateOf(false) }

    if (progressIndicator.value){
        Box(
            modifier = progressbarModifier
        ) {
            CircularProgressIndicator(
                color = progressbarColor
            )
        }
    }

    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier
    ) {
        val state = painter.state
        if (
            state is AsyncImagePainter.State.Loading
        ) {
            progressIndicator.value = true
        } else {
            progressIndicator.value = false
            SubcomposeAsyncImageContent()
        }
    }
}