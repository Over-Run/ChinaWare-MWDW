package org.overrun.vmdw.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.ApplicationScope

/**
 * @author baka4n
 * @since Switch invokes the window
 */
class Switch: @Composable MutableMap<String, @Composable ApplicationScope.() -> Unit> , HashMap<String, @Composable ApplicationScope.() -> Unit>() {
    @Composable
    fun add(key: String, content: @Composable ApplicationScope.() -> Unit): Switch {
        this[key] = content
        return this
    }

    @Composable
    fun build(scope: ApplicationScope, mode: MutableState<String>) {
        scope.run {
            for ((k,v) in this@Switch) if (k == mode.value) return@run v
        }
    }


}
