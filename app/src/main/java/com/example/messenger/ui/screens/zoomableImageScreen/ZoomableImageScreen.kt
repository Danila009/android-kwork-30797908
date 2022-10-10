package com.example.messenger.ui.screens.zoomableImageScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.messenger.ui.screens.chatScreen.imageUrl
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.view.ZoomableImage

@Composable
fun ZoomableImageScreen(
    navController: NavController,
    url: String
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    Log.e("urlImage",url)

    ZoomableImage(
        painter = rememberAsyncImagePainter(
            model = imageUrl
        ),
        isRotation = false,
        modifier = Modifier.size(
            width = screenWidthDp,
            height = screenHeightDp
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier.padding(15.dp),
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = primaryText
            )
        }
    }
}