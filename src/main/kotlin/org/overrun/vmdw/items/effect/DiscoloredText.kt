package org.overrun.vmdw.items.effect

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DiscoloredText(
    state: ScrollState,
    color: Color,
    color1: Color,
    i: Int,
    text: String
) {
    val color2 = if (state.value == i) { color1 } else { color }
    Text(
        text = text,
        modifier = Modifier.background(color2)
    )
}
