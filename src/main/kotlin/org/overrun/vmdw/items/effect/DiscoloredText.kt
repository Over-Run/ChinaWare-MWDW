package org.overrun.vmdw.items.effect

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.graphics.Color

class DiscoloredText(
    val state: ScrollState
) {

    fun add() {
        val color: Color = if (state.value == 0) Color.Magenta else Color.Red
    }
}
