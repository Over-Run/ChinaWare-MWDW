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

package org.overrun.vmdw.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

const val CONFIG_LANG_KEY = "language"
const val CONFIG_LANG_DEF = "en_us"

/**
 * @author baka4n, squid233
 * @since 0.1.0
 */
object Config {
    var language: String = CONFIG_LANG_DEF
        set
    var last_mod: String = ""
        set
    var is_open_last_mod = true
        set
    val file = File(System.getProperty("user.dir"), ".vmdw/config.json")
    private val configMap = HashMap<String, String>()

    fun get(key: String, def: Comparable<*>): Comparable<*> = configMap.getOrDefault(key, def)
    operator fun get(key: String): Comparable<*>? = configMap[key]

    fun set(autoSave: Boolean = true, action: (Config) -> Unit) {
        action(this)
        configMap[CONFIG_LANG_KEY] = language
        configMap["last_mod"] = last_mod
        configMap["is_open_last_mod"] = is_open_last_mod.toString()
        if (autoSave) save()
    }

    fun init() {
        file.parentFile.also { if (!it.exists()) it.mkdirs() }
        if (!file.exists()) {
            javaClass.classLoader.getResourceAsStream("config.json")!!.buffered().use {
                file.outputStream().buffered().use(it::transferTo)
            }
        }
        load()
    }

    /**
     * load config.json
     */
    @OptIn(ExperimentalSerializationApi::class)

    fun load() {
        file.inputStream().buffered().use {
            configMap.putAll(Json.decodeFromStream(it))
        }
        language = get(CONFIG_LANG_KEY, CONFIG_LANG_DEF) as String
        last_mod = get("last_mod", "") as String
        is_open_last_mod = get("is_open_last_mod", "true") as Boolean
    }

    /**
     * Saves the configurations.
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun save() {
        BufferedWriter(FileWriter(file)).use {

            Json.encodeToStream(configMap, file.outputStream())
        }
//        Files.writeString(path, Json.encodeToString(configMap))
    }
}
