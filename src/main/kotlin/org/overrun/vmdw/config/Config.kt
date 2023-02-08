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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.nio.file.Files

const val CONFIG_LANG_KEY = "language"
const val CONFIG_LANG_DEF = "en_us"

/**
 * @author baka4n, squid233
 * @since 0.1.0
 */
object Config {
    var language: String = CONFIG_LANG_DEF
        private set
    val file = File(System.getProperty("user.dir"), ".vmdw/config.json")
    val path = file.toPath()
    private val configMap: MutableMap<String, String> = HashMap()

    fun get(key: String, def: String): String = configMap.getOrDefault(key, def)
    operator fun get(key: String): String? = configMap[key]

    fun set(autoSave: Boolean = true, action: (Config) -> Unit) {
        action(this)
        configMap[CONFIG_LANG_KEY] = language
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
        language = get(CONFIG_LANG_KEY, CONFIG_LANG_DEF)
    }

    /**
     * Saves the configurations.
     */
    fun save() {
        Files.writeString(path, Json.encodeToString(configMap))
    }
}
