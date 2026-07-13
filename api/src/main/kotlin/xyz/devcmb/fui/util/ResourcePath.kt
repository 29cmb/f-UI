package xyz.devcmb.fui.util

import java.io.File

class ResourcePath internal constructor(vararg parts: String) {
    internal constructor(path: String): this(*path.split("/").toTypedArray())

    val path = parts.joinToString("/")
    fun toFilePath(): String {
        return path.replace("/", File.separator)
    }
}