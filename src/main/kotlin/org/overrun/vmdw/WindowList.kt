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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.WindowPosition
import org.overrun.vmdw.config.BuildSrc
import org.overrun.vmdw.config.Config
import org.overrun.vmdw.items.effect.DiscoloredText
import org.overrun.vmdw.items.effect.DropdownMenuList
import org.overrun.vmdw.items.effect.RollingEffect
import org.overrun.vmdw.items.effect.RowTextAndFieldEffect
import org.overrun.vmdw.items.window.SettingsDialog
import java.awt.Desktop
import java.net.URI

@OptIn(ExperimentalUnitApi::class)
@Composable
fun create(mode: MutableState<String>) {
    Dialog(
        onCloseRequest = {mode.value = "off"},
        title = I18n["menu.file.create"],
        state = DialogState(
            width = 380.dp,
            height = 400.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = false
    ) {
        val modid = remember { mutableStateOf("") }
        val authors = remember { mutableStateOf("") }
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
                vauleModel = authors,
                tooltipText = I18n["create.contributor.text.field.tooltip"],
                textUnit = TextUnit(12f, TextUnitType.Sp)
            )

            Button(
                onClick = {
                    BuildSrc.load(modid.value)
                    BuildSrc.build!!.getModSettings.setModid(modid.value)
                    BuildSrc.build!!.getModSettings.setAuthors(authors.value)
                    BuildSrc.build!!.propertiesTools.save("save mod settings.")
                    mode.value = "off"
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(I18n["button.create"])
            }
        }.build()

    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun about(mode: MutableState<String>) {
     Dialog(
        onCloseRequest = { mode.value = "off" },
        title = I18n["dialog.about.title"],
        state = DialogState(
            width = 300.dp,
            height = 300.dp,
            position = WindowPosition(Alignment.Center)
        ),
         resizable = false
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
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                )
            }
            Text(text = "  ChinaWare VMDW full name is ChinaWare Visualize Minecraft Development Wheel")
        }
    }
}
@Composable
fun settings(mode: MutableState<String>, map: MutableMap<String, String>, language: MutableState<String>) {
    val buttonText = remember { mutableStateOf(map[language.value]) }
    val drop = remember { mutableStateOf(false) }
    SettingsDialog(
        mode = mode,
        width = 800,
        height = 640,
        alignment = Alignment.Center
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
                DropdownMenuList(drop, language, map, buttonText)
            }
        }.build()
        Button(
            onClick = {
                mode.value = "off"
                Config.set { it.language = language.value.trim() }
                I18n.init()
                Config.save()
            },
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.BottomCenter)
        ) {
            Text(I18n["settings.save"])
        }
    }
}
