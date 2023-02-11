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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import org.overrun.vmdw.config.CONFIG_LANG_DEF
import org.overrun.vmdw.config.Config
import org.overrun.vmdw.items.effect.DiscoloredText
import org.overrun.vmdw.items.effect.DropdownMenuList
import org.overrun.vmdw.items.effect.RollingEffect
import org.overrun.vmdw.items.window.SettingsDialog
import java.awt.Desktop
import java.net.URI

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
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
fun main() {
    // Note: Do NOT use expression body. That cause the initialization reruns.
    Config.init()
    I18n.init()

    application {
        val isOpen = remember { mutableStateOf(true) }
        var isAboutOpen by remember { mutableStateOf(false) }
        val isSettingOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
        val width by remember { mutableStateOf(Config.get("width", "1000").toInt()) }
        val height by remember { mutableStateOf(Config.get("height", "800").toInt()) }
        val language = remember { mutableStateOf(Config.get("language", CONFIG_LANG_DEF)) }
        val map: MutableMap<String, String> = HashMap()
        map["en_us"] = "english[us]"
        map["zh_cn"] = "简体中文"
        val buttonText = remember { mutableStateOf(map[language.value]) }
        val drop = remember { mutableStateOf(false) }
        if (isOpen.value) {
            Window(
                onCloseRequest = { isOpen.value = false },
                title = "ChinaWare VMDW",
                state = WindowState(
                    size = DpSize(width.dp, height.dp),
                    position = WindowPosition(Alignment.Center)
                ),
                icon = painterResource("icon.png")
            ) {
                MenuBar {
                    Menu(I18n["menu.file"], mnemonic = 'F') {
                        Item(
                            I18n["menu.file.settings"],
                            mnemonic = 'T',
                            shortcut = KeyShortcut(Key.S, ctrl = true, alt = true),
                        ) {
                            isSettingOpen.value = true
                        }
                        Item(I18n["menu.file.exit"], mnemonic = 'X') { isOpen.value = false }
                    }
                    Menu(I18n["menu.edit"], mnemonic = 'E') { }
                    Menu(I18n["menu.view"], mnemonic = 'V') { }
                    Menu(I18n["menu.help"], mnemonic = 'H') {
                        Item(I18n["menu.help.about"], mnemonic = 'A') { isAboutOpen = true }
                    }
                }
                windowContent(this)
            }
        }
        if (isAboutOpen) {
            Dialog(
                onCloseRequest = { isAboutOpen = false },
                title = I18n["dialog.about.title"],
                state = DialogState(
                    width = 300.dp,
                    height = 300.dp,
                    position = WindowPosition(Alignment.Center)
                )
            ) {
                // 关于内容
                Column {
                    TextButton(
                        onClick = {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().also {
                                    if (it.isSupported(Desktop.Action.BROWSE)) {
                                        it.browse(URI.create("https://github.com/Over-Run/ChinaWare-VMDW"))
                                    }
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(1.dp)
                    ) {
                        Text(
                            text = "ChinaWare VMDW",
                            color = Color.Blue,
                            fontSize = TextUnit(12f, TextUnitType.Sp))
                    }
                    Text(text = "  ChinaWare VMDW full name is ChinaWare Visualize Minecraft Development Wheel")
                }
            }
        }

        if (isSettingOpen.value) {
            SettingsDialog(
                isSettingOpen = isSettingOpen,
                width = 800,
                height = 640,
                alignment = Alignment.Center
            ) {
                val state = rememberScrollState()
                RollingEffect(
                    modifier =
                        Modifier
                            .background(Color.LightGray)
                            .fillMaxSize()
                ).add(
                    Modifier.padding(end = 100.dp, bottom = 50.dp).background(Color.Red).fillMaxSize(),
                ) {
                    DiscoloredText(
                        state = state,
                        color = Color.Red,
                        color1 = Color.Magenta,
                        i = 0,
                        text = I18n["settings.language"]
                    )
                }.add(
                    Modifier
                        .padding(start = 100.dp, end = 130.dp, top = 0.dp, bottom = 50.dp)
                        .fillMaxSize()
                        .background(Color.LightGray)
                ).add(
                    Modifier
                        .padding(start = 130.dp,end = 100.dp, bottom = 50.dp)
                        .background(Color.Green)
                        .fillMaxSize()
                        .verticalScroll(state)
                ) {
                    Button(
                        onClick = {
                            drop.value = true
                        },
                        modifier = Modifier
                    ) {
                        Text(text = buttonText.value!!)
                    }
                    Row {
                        DropdownMenuList(drop, language, map, buttonText)
                    }

                    repeat(100) {
                        Text("test${it + 1}")
                    }
                }.build()
                Button(
                    onClick = { isSettingOpen.value = false },
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)
                ) {
                    Text(I18n["settings.save"])
                    Config.set { it.language = language.value.trim() }
                    I18n.init()
                    Config.save()
                }

            }
        }
    }
}
