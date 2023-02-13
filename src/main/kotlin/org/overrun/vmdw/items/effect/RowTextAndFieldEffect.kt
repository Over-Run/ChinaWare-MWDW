package org.overrun.vmdw.items.effect

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
fun RowTextAndFieldEffect(
    text: String,
    vauleModel: MutableState<String>,
    tooltipText: String,
    textUnit: TextUnit,
    t: (String) -> Unit = {vauleModel.value = it}
) {
    row {
        TextButton(
            onClick = {},
            enabled = false,
            content = {
                Text(text)
            },

        )
        TextField(
            value = vauleModel.value,
            onValueChange = t,
            placeholder = { Text(
                text = tooltipText,
                fontSize = textUnit
            )}
        )
    }
}
@Composable
fun row(content: @Composable RowScope.() -> Unit) = Row(
    modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.End),
    content = content
)
