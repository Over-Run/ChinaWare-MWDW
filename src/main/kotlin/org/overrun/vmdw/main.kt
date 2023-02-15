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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import org.overrun.vmdw.config.BuildSrc
import org.overrun.vmdw.config.CONFIG_LANG_DEF
import org.overrun.vmdw.config.Config
import java.io.File

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

fun SnapshotStateMap<String, String>.setMode(string: String) {
    if (this["mode"]!! == "off") {
        this["mode"] = string
    }
}

/**
 * @author baka4n
 * @since 无法同时打开关于,创建,设置窗口
 */
fun MutableState<String>.setMode(string: String) {
    if (this.value == "off") {
        this.value = string
    }
}

/**
 * @author squid233, baka4n
 * @since 0.1.0
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Note: Do NOT use expression body. That cause the initialization reruns.
    Config.init()
    I18n.init()
    val a: Comparable<*> = if (true) 1 else "aa"

    application {
        val booleanRemember = remember {
            mutableStateMapOf(
                Pair("isOpen", true),
                Pair("isOpenFile", true)
            )
        }
        val stringRemember: SnapshotStateMap<String, String> = remember {
            mutableStateMapOf(
                Pair("title", "ChinaWare VMDW"),
                Pair("language", Config.get("language", CONFIG_LANG_DEF) as String),
                Pair("mode", "off")
            )
        }
        val intRemember: SnapshotStateMap<String, Int> = remember {
            mutableStateMapOf(
                Pair("width", Config.get("width", 1000) as Int),
                Pair("height", Config.get("height", 800) as Int)
            )
        }
        val languageSelectMap: MutableMap<String, String> = mutableMapOf(
            Pair("en_us", "english[us]"),
            Pair("zh_cn", "简体中文")
        )
        // last mod 判断
        if (Config["is_open_last_mod"] as Boolean) {
            if (!Config.last_mod.isEmpty()) {
                BuildSrc.openDirection = File(System.getProperty("user.dir"), ".vmdw/buildSrc/${Config.last_mod}")
                if (BuildSrc.openDirection!!.exists()) {
                    Config.set {
                        it.last_mod = ""
                    }
                    BuildSrc.openDirection = null
                    booleanRemember["stringRemember"] = true
                }
            }
        }
        //
        if (booleanRemember["isOpen"]!!)
            defaultWindow(booleanRemember, stringRemember, intRemember)
        when(stringRemember["mode"]) {
            "about" -> newAbout(stringRemember)
            "settings" -> newSettings(languageSelectMap, stringRemember)
            "create" -> newCreate(stringRemember, booleanRemember)
            "open" -> fileOpen(stringRemember)
        }
    }
}
