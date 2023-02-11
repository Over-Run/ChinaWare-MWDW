package org.overrun.vmdw.items.effect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class RollingEffect(
    var modifier: Modifier =
        Modifier
            .background(Color.LightGray)
            .fillMaxSize(),
) {
    private val map: MutableMap<Int, Record> = HashMap()
    @Composable
    fun add(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit = {}): RollingEffect {
        map[map.size] = Record(modifier, content)
        return this
    }

    @Composable
    fun remove(int: Int) {
        map.remove(int)
    }

    @Composable
    fun build() {
        Boxes(modifier = modifier) {
            for ((_, u) in map) {
                Column(modifier = u.modifier, content = u.content)
            }
        }
    }
}
@Composable
private fun Boxes(modifier: Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier = modifier, content = content)
}
data class Record(val modifier: Modifier, val content: @Composable ColumnScope.() -> Unit)
