package org.overrun.vmdw.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScrollBoxes(modifier: Modifier,content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier, content = content)
}

@Composable
fun Repeat(i: Int = 10, action: (Int) -> Unit) {//default i = 10
    repeat(i) { action(it) }
}
