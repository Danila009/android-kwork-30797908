package com.example.messenger.ui.view

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

val mask = "1234  5678  1234  5678"

fun mobileNumberFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            append(trimmed[i])
            if (i % 4 == 3 && i != 15) {
                append("  ")
            }
        }
        pushStyle(SpanStyle(color = Color.LightGray))
        append(mask.takeLast(mask.length - length))
        toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 2
            if (offset <= 11) return offset + 4
            if (offset <= 16) return offset + 6
            return 22
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 2
            if (offset <= 14) return offset - 4
            if (offset <= 19) return offset - 6
            return 16
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}
