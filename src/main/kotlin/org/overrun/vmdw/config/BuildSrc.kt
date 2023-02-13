package org.overrun.vmdw.config

import java.io.*
import java.util.*

object BuildSrc {
    var build: Build? = null
    fun load(fileName: String) {
        build = Build(fileName)
    }
}

class Build(fileName: String) {
    private val dir = File(System.getProperty("user.dir"), ".vmdw/buildSrc/${fileName}")
    init {
        if (!dir.exists()) dir.mkdirs()
    }
    val modSettings = File(dir, "modSettings.properties")
    var propertiesTools: PropertiesTools = PropertiesTools(modSettings)
    var getModSettings: GetModSettings
    init {
        propertiesTools.init()
        getModSettings = GetModSettings(propertiesTools)
    }
}

class GetModSettings(var propertiesTools: PropertiesTools) {
    fun setModid(modid: String) {
        propertiesTools.put("modid", modid)
    }

    fun setContributors(contributors: String) {
        propertiesTools.put("contributors", contributors)
    }
    fun setAuthors(vararg authors: String) {
        val sb = StringBuilder()

        authors.forEachIndexed { index, it ->
            run {
                sb.append(it)
                if (authors.size - 1 != index) {
                    sb.append(",")
                }
            }
        }
        propertiesTools.put("authors", sb.toString())
    }
}

class PropertiesTools(private val f: File): Properties() {
    fun init() {
        ifs()
        try {
            load()
        } catch (e: FileNotFoundException) {
            try {
                save("save mod settings.")
            } catch (f: IOException) {
                f.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun ifs() {
        if (!this.exists() && !this.isDirectory()) f.mkdirs()
    }

    fun put(key: String?, value: String?) { this[key] = value }

    fun save(title: String) { store(title) }

    fun load() { this.load(BufferedInputStream(FileInputStream(f))) }

    private fun exists(): Boolean { return f.exists() }

    private fun isDirectory(): Boolean { return f.parentFile.isDirectory }

    fun store(title: String?) {
        this.store(BufferedOutputStream(FileOutputStream(f)), title)
    }
}
