package org.overrun.vmdw.config

import com.google.gson.Gson
import java.io.File

fun main() {
    ConfigNew.init()
    ConfigNew.forEach { t, u ->
        println(t)
        println(u)
    }
    ConfigNew.save()
}

object ConfigNew: HashMap<Any?, Any?>() {

    val tMap: MutableMap<Any?, Any?> = HashMap()
    val file = File(System.getProperty("user.dir"), ".vmdw/config.json")
    val gson: Gson = Gson()

    fun init() {
        Config.file.parentFile.also { if (!it.exists()) it.mkdirs() }
        if (!Config.file.exists()) {
            javaClass.classLoader.getResourceAsStream("config.json")!!.buffered().use {
                file.outputStream().buffered().use(it::transferTo)
            }
        }
        load()
    }

    fun load() {
        Config.file.inputStream().buffered().bufferedReader().use {
            putAll(gson.fromJson(it, MutableMap::class.java))
        }
        tMap.putAll(this)
    }

    /**
     * @author baka4n
     * If you are simply saving the setting in this run, you can change true to false
     */
    fun save() {
        putAll(tMap)
        file.bufferedWriter().use {
            it.write(gson.toJson(this))
        }
    }
}
