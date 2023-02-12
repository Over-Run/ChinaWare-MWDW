package org.overrun.vmdw.items.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindowScope
import androidx.compose.ui.window.WindowPosition
import org.overrun.vmdw.I18n

@Composable
fun SettingsDialog(
    mode: MutableState<String>,
    key: String = "dialog.settings.title",
    width: Int, height: Int, alignment: Alignment,
    content: @Composable DialogWindowScope.() -> Unit
) {
    dialog(mode, key, width, height, alignment, content)
}

@Composable
fun dialog(
    mode: MutableState<String>,
    key: String,
    width: Int, height: Int,
    alignment: Alignment,
    content: @Composable DialogWindowScope.() -> Unit
) {
    Dialog(
        onCloseRequest = {
            mode.value = "off"
        },
        title = I18n[key],
        state = DialogState(
            width = width.dp,
            height = height.dp,
            position = WindowPosition(alignment)
        ),
        content = content
    )
}
