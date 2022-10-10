package com.example.messenger.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.messenger.R

enum class Animation(val resId: Int){
    Auth(R.raw.auth),
    CheckCodePhone(R.raw.check_code_phone),
    Empty(R.raw.empty),
    GradientInfiniteSign(R.raw.gradient_infinite_sign),
    LoadingCirclesModel(R.raw.loading_circles_model),
    EditorUsername(R.raw.editor_username),
    Writing(R.raw.writing),
    Hi(R.raw.hi)
}

@Composable
fun LottieAnimation(
    animation: Animation,
    modifier: Modifier = Modifier,
    iterations:Int = LottieConstants.IterateForever
) {
    val compositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animation.resId))

    val progress = animateLottieCompositionAsState(
        composition = compositionResult.value,
        iterations = iterations,
    )

    com.airbnb.lottie.compose.LottieAnimation(
        composition = compositionResult.value,
        progress = progress.progress,
        modifier = modifier
    )

}