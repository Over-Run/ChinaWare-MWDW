package org.overrun.vmdw.items

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun DropdownMenuList(drop: MutableState<Boolean>, key: MutableState<String>, map: MutableMap<String, String>) {
    dropDownMenuList(drop, key, map)
}

@Composable
fun dropDownMenuList(drop: MutableState<Boolean>, key: MutableState<String>, map: MutableMap<String, String>) {
    DropdownMenu(
        expanded = drop.value,
        onDismissRequest = {
            drop.value = !drop.value
        }
    ) {
        for ((t, u) in map) {
            DropdownMenuItem(
                onClick = {
                    drop.value = false
                    key.value = u
                }
            ) {
                Text(t)
            }
        }
    }
}
