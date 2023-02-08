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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.overrun.vmdw.config.CONFIG_LANG_DEF
import org.overrun.vmdw.config.Config
import java.io.FileNotFoundException

/**
 * @author baka4n, squid233
 * @since 0.1.0
 */
object I18n {
    private val i18nTranslate: MutableMap<String, MutableMap<String, String>> = HashMap()

    fun translate(lang: String, key: String): String =
        i18nTranslate[lang]?.getOrDefault(key, i18nTranslate[CONFIG_LANG_DEF]?.get(key)) ?: key

    operator fun get(key: String): String =
        translate(Config.language, key)

    @OptIn(ExperimentalSerializationApi::class)
    fun init(lang: String) {
        javaClass.classLoader.getResourceAsStream("lang/${lang}.json")?.buffered()?.use {
            i18nTranslate.computeIfAbsent(lang) { HashMap() }.apply {
                putAll(Json.decodeFromStream(it))
            }
        } ?: throw FileNotFoundException("lang/${lang}.json")
    }

    fun init() {
        init(Config.language)
    }
}
