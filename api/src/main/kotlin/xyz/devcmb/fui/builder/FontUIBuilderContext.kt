package xyz.devcmb.fui.builder

import net.kyori.adventure.key.Key
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.font.DefaultAscentFont
import xyz.devcmb.fui.font.FontGlyph
import xyz.devcmb.fui.font.FontParser
import xyz.devcmb.fui.util.FontUILogger
import java.io.File
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.io.path.exists

class FontUIBuilderContext internal constructor(pack: File) {
    private val fontUILogger = FontUILogger(this)

    internal val fs: FileSystem = FileSystems.newFileSystem(pack.toPath())

    val glyphs: HashMap<Key, ArrayList<FontGlyph>> = HashMap()
    val defaultFontAscents: ArrayList<DefaultAscentFont> = ArrayList()
    val spacingCharacters: HashMap<Key, Map<Char, Double>> = HashMap()

    internal val fontParser = FontParser(this)

    var debug = false

    fun registerFont(key: Key) {
        val path = fs.getPath("assets", key.namespace(), "font", "${key.value()}.json")
        if(!path.exists()) throw IllegalArgumentException("Font ${key.asMinimalString()} was not found")

        Files.newBufferedReader(path).use { reader ->
            val glyphs = fontParser.parseFontFile(reader)
            fontUILogger.log(glyphs.toString())
            this.glyphs[key] = glyphs
        }

        fontUILogger.log("Registered font ${key.asMinimalString()}")
    }

    /** Builds the generated assets to a FontUI instance */
    internal fun build(): FontUI {
        return FontUI(glyphs, defaultFontAscents, spacingCharacters, fontUILogger)
    }

    /**
     * Registers fonts that use the vanilla assets, but with a different ascent value.
     *
     * Vanilla font files can be found at [mcasset.cloud](https://mcasset.cloud)
     *
     * @param ascents Ascents of the font files
     * @param getFontKey A lambda function that is run to get the [Key] of each font file, given its ascent
     */
    fun registerDefaultAscents(vararg ascents: Int, getFontKey: (ascent: Int) -> Key) {
        ascents.forEach {
            val fontKey = getFontKey(it)
            val path = fs.getPath("assets", fontKey.namespace(), "font", "${fontKey.value()}.json")
            if(!path.exists()) throw IllegalArgumentException("Default ascent font ${fontKey.asMinimalString()} (ascent $it) was not found")

            defaultFontAscents.add(DefaultAscentFont(it, fontKey))
        }
    }

    /**
     * Registers negative spacing characters
     *
     * You can technically use multiple fonts, but only one should work for most projects.
     *
     * @param chars The map of the characters to their spacing. These values can be doubles for very specfic glyph spacing
     * @param font The font with the characters
     */
    fun registerSpacingCharacters(chars: Map<Char, Double>, font: Key) {
        val path = fs.getPath("assets", font.namespace(), "font", "${font.value()}.json")
        if(!path.exists()) throw IllegalArgumentException("Spaces font ${font.asMinimalString()} was not found")

        fontUILogger.log(chars.toString())
        spacingCharacters[font] = chars
    }
}