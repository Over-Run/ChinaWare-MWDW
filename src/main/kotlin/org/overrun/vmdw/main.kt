/*
 * MIT License
 *
 * Copyright (c) 2023 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.overrun.vmdw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.overrun.vmdw.config.Config

/**
 * @author baka4n, squid233
 * @since 0.1.0
 */

@Composable
fun windowContent(scope: FrameWindowScope) = scope.run {
    var text by remember { mutableStateOf("test ui") }
    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    text = "test button"
                }
            ) {
                Text(text)
            }
        }
    }

}

/**
 * @author squid233, baka4n
 * @since 0.1.0
 * todo 23/2/6 rewrite -> isOpen -> close window
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Config.init()
    I18n.init()
    var isOpen by remember { mutableStateOf(true) }
    if (isOpen) {
        Window(
            onCloseRequest = {isOpen = false},
            title = "ChinaWare VMDW",
            state = rememberWindowState(width = 300.dp, height = 300.dp),
            icon = painterResource("icon.png")
        ) {
            MenuBar {
                Menu(I18n["menu.file"], mnemonic = 'F') {
                    Item(I18n["file.close"], onClick = { isOpen = false })
                }
                Menu(I18n["menu.edit"], mnemonic = 'E') {
                    Item(I18n["edit.language"], onClick = {
                        
                    })
                }
            }
            windowContent(this)

        }
    }
}
