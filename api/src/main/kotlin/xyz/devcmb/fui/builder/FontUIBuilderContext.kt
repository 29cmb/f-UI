package xyz.devcmb.fui.builder

import net.kyori.adventure.key.Key
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.font.FontGlyph
import xyz.devcmb.fui.font.FontParser
import xyz.devcmb.fui.util.Logger
import java.io.File
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.io.path.exists

class FontUIBuilderContext(pack: File) {
    private val logger = Logger(this)

    val fs: FileSystem = FileSystems.newFileSystem(pack.toPath())
    val glyphs: HashMap<Key, ArrayList<FontGlyph>> = HashMap()

    val fontUI = FontUI(glyphs)
    val fontParser = FontParser(this)

    var debug = false

    fun registerFont(key: Key) {
        val path = fs.getPath("assets", key.namespace(), "font", "${key.value()}.json")
        if(!path.exists()) throw IllegalArgumentException("Font ${key.asMinimalString()} was not found")

        Files.newBufferedReader(path).use { reader ->
            val glyphs = fontParser.parseFontFile(reader)
            logger.log(glyphs.toString())
            this.glyphs[key] = glyphs
        }

        logger.log("Registered font ${key.asMinimalString()}")
    }
}