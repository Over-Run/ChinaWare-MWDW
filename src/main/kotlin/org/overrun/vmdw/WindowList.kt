@file:Suppress("DuplicatedCode")

package org.overrun.vmdw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import org.overrun.vmdw.config.BuildSrc
import org.overrun.vmdw.config.Config
import org.overrun.vmdw.items.effect.DiscoloredText
import org.overrun.vmdw.items.effect.DropdownMenuList
import org.overrun.vmdw.items.effect.RollingEffect
import org.overrun.vmdw.items.effect.RowTextAndFieldEffect
import java.awt.Desktop
import java.io.File
import java.net.URI
import javax.swing.JFileChooser

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun defaultWindow(
    booleanRemember: SnapshotStateMap<String, Boolean>,
    stringRemember: SnapshotStateMap<String, String>,
    intRemember: SnapshotStateMap<String, Int>
) {
    Window(
        onCloseRequest = {
            booleanRemember["isOpen"] = false
        },
        title = stringRemember["title"]!!,
        state = WindowState(
            size = DpSize(intRemember["width"]!!.dp, intRemember["height"]!!.dp),
            position = WindowPosition(Alignment.Center)
        ),
        icon = painterResource("icon.png")
    ) {
        MenuBar {
            Menu(I18n["menu.file"], mnemonic = 'F') {
                Item(
                    I18n["menu.file.create"],
                    mnemonic = 'C',
                    shortcut = KeyShortcut(Key.C, ctrl = true, alt = true),
                    enabled = booleanRemember["isOpenFile"]!!
                ) {
                    stringRemember.setMode("create")
                }
                Item(
                    I18n["menu.file.open"],
                    mnemonic = 'O',
                    shortcut = KeyShortcut(Key.O, ctrl = true, shift = true, alt = true),
                    enabled = booleanRemember["isOpenFile"]!!
                ) {
                    stringRemember.setMode("open")
                }
                Item(
                    I18n["menu.file.close"],
                    mnemonic = 'C',
                    enabled = !booleanRemember["isOpenFile"]!!
                ) {
                    BuildSrc.openDirection = null
                    booleanRemember["isOpenFile"] = !booleanRemember["isOpenFile"]!!
                }
                Item(
                    I18n["menu.file.settings"],
                    mnemonic = 'T',
                    shortcut = KeyShortcut(Key.S, ctrl = true, alt = true),
                ) {
//                            isSettingOpen.value = true
                    stringRemember.setMode("settings")
                }
                Item(I18n["menu.file.exit"], mnemonic = 'X') { booleanRemember["isOpen"] = false }
            }
            Menu(I18n["menu.edit"], mnemonic = 'E') { }
            Menu(I18n["menu.view"], mnemonic = 'V') { }
            Menu(I18n["menu.help"], mnemonic = 'H') {
                Item(I18n["menu.help.about"], mnemonic = 'A') {
//                            isAboutOpen = true
                    stringRemember.setMode("about")
                }
            }
        }
        windowContent(this)
    }
}

/**
 * @author baka4n
 * Use swing -> JFileChooser class
 */
fun fileOpen(stringRemember: SnapshotStateMap<String, String>) {
    JFileChooser(File(System.getProperty("user.dir"))).apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        showOpenDialog(ComposeWindow())
        BuildSrc.openDirection = selectedFile?.absoluteFile
        stringRemember["mode"] = "off"
    }
}
/**
 * @author baka4n
 */
@OptIn(ExperimentalUnitApi::class)
@Composable
fun newCreate(stringRemember: SnapshotStateMap<String, String>, booleanRemember: SnapshotStateMap<String, Boolean>) {
    Window(
        onCloseRequest = {stringRemember["mode"] = "off"},
        title = I18n["menu.file.create"],
        state = WindowState(
            width = 380.dp,
            height = 400.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = false,
        alwaysOnTop = true
    ) {
        val modid = remember { mutableStateOf("") }
        val authors = remember { mutableStateOf("") }
        val contributors = remember { mutableStateOf("") }
        RollingEffect(
            modifier = Modifier.fillMaxSize().padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
        ).add(
            modifier = Modifier.padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 100.dp)
        ) {
            RowTextAndFieldEffect(
                text = I18n["create.modid.title"],
                vauleModel = modid,
                tooltipText = I18n["create.modid.text.field.tooltip"],
                textUnit = TextUnit(12f, TextUnitType.Sp),
                t = {
                    var b = false
                    if (it.isEmpty()) b = true else
                        for (i in it.indices) {
                            val c = it[i]
                            if (
                                c in 'a'..'z' ||
                                c in 'A'..'Z' ||
                                c in '_'..'_' ||
                                c in '-'..'-'
                            ) b = true
                        }


                    when {
                        b -> modid.value = it
                        else -> {
                            if (it.length < modid.value.length)
                                modid.value = modid.value.substring(0, modid.value.length - 2)
                            else
                                modid.value = modid.value
                        }
                    }

                }
            )
            RowTextAndFieldEffect(
                text = I18n["create.authors.title"],
                vauleModel = authors,
                tooltipText = I18n["create.author.text.field.tooltip"],
                textUnit = TextUnit(12f, TextUnitType.Sp)
            )
            RowTextAndFieldEffect(
                text = I18n["create.contributor.title"],
                vauleModel = contributors,
                tooltipText = I18n["create.contributor.text.field.tooltip"],
                textUnit = TextUnit(12f, TextUnitType.Sp)
            )

            Button(
                onClick = {
                    if (modid.value.isNotEmpty() && authors.value.isNotEmpty()) {
                        BuildSrc.load(modid.value)
                        BuildSrc.build!!.getModSettings.setModid(modid.value)
                        BuildSrc.build!!.getModSettings.setAuthors(authors.value)
                        BuildSrc.build!!.getModSettings.setContributors(contributors.value)
                        BuildSrc.build!!.propertiesTools.save("save mod settings.")
                        BuildSrc.openDirection = BuildSrc.build!!.dir
                        if (BuildSrc.openDirection != null) {
                            booleanRemember["isOpenFile"] = !booleanRemember["isOpenFile"]!!
                        }
                        stringRemember["mode"] = "off"
                    }


                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(I18n["button.create"])
            }
        }.build()
    }
}

/**
 * @author baka4n
 */

@OptIn(ExperimentalUnitApi::class)
@Composable
fun newAbout(stringRemember: SnapshotStateMap<String, String>) {
    Window(
        onCloseRequest = {stringRemember["mode"] = "off"},
        title = I18n["dialog.about.title"],
        state = WindowState(
            width = 300.dp,
            height = 300.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = false,
        alwaysOnTop = true,
        icon = painterResource("icon.png")
    ) {
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
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }
            Text(text = "  ChinaWare VMDW full name is ChinaWare Visualize Minecraft Development Wheel")
        }
    }
}
/**
 * @author baka4n
 */
@Composable
fun newSettings(
    map: MutableMap<String, String>, stringRemember: SnapshotStateMap<String, String>
) {
    val buttonText = remember { mutableStateOf(map[stringRemember["language"]]) }
    val drop = remember { mutableStateOf(false) }
    Window(
        onCloseRequest = {stringRemember["mode"] = "off"},
        title = I18n["dialog.settings.title"],
        state = WindowState(
            width = 800.dp,
            height = 640.dp,
            position = WindowPosition(Alignment.Center)
        ),
        alwaysOnTop = true
    ) {
        val state = rememberScrollState()
        RollingEffect(
            modifier =
            Modifier
                .fillMaxSize()
        ).add(
            Modifier.padding(end = 700.dp, bottom = 50.dp).background(Color.Red).fillMaxSize(),
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
                DropdownMenuList(drop, stringRemember, map, buttonText)
            }
        }.build()
        Button(
            onClick = {
                stringRemember["mode"] = "off"
                Config.set { it.language = stringRemember["language"]!! }
                I18n.init()
            },
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)
        ) {
            Text(I18n["settings.save"])
        }
    }
}
