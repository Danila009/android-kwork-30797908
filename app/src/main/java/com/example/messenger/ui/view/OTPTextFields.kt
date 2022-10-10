package com.example.messenger.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPTextFields(
    modifier: Modifier = Modifier,
    length: Int = 5,
    value: String = "",
    onValueChanged: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = value,
        onValueChange = {
            if (it.length <= length) {
                if (it.all { c -> c in '0'..'9' }) {
                    onValueChanged(it)
                }
                if (it.length >= length) {
                    keyboard?.hide()
                }
            }
        },
        // Hide the text field
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(length) {
            OtpCell(
                modifier = modifier
                    .size(width = 45.dp, height = 60.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colors.surface)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboard?.show()
                    },
                value = value.getOrNull(it)?.toString() ?: "",
                isCursorVisible = value.length == it
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun OtpCell(
    modifier: Modifier,
    value: String,
    isCursorVisible: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        Text(
            text = if (isCursorVisible) cursorSymbol else value,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
