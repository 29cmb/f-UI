package xyz.devcmb.fui.util

import java.io.File

class ResourcePath(vararg parts: String) {
    constructor(path: String): this(*path.split("/").toTypedArray())

    val path = parts.joinToString("/")
    fun toFilePath(): String {
        return path.replace("/", File.separator)
    }
}