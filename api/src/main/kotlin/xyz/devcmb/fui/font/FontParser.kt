package xyz.devcmb.fui.font

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import xyz.devcmb.fui.builder.FontUIBuilderContext
import xyz.devcmb.fui.util.IdentifiedResource
import xyz.devcmb.fui.util.trimmedWidth
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.exists

class FontParser(val builderContext: FontUIBuilderContext) {
    fun parseFontFile(stream: BufferedReader): ArrayList<FontGlyph> {
        val contents = stream.readText()

        val jsonObject = Json.parseToJsonElement(contents).jsonObject
        val providers = jsonObject["providers"]?.jsonArray ?: throw IllegalArgumentException("Provided file is not a valid font file")

        val glyphs: ArrayList<FontGlyph> = ArrayList()
        providers.forEach {
            val obj = it.jsonObject
            val providerGlyph = when(val decoded = Json.decodeFromJsonElement<FontProvider>(obj)) {
                is FontProvider.BitmapFontProvider -> parseBitmapFontProvider(decoded)
            }
            glyphs.add(providerGlyph)
        }

        return glyphs
    }

    fun parseBitmapFontProvider(provider: FontProvider.BitmapFontProvider): FontGlyph {
        val path = builderContext.fs.getPath(Path.of(
            "assets",
            provider.file.namespace,
            "textures",
            provider.file.resourcePath.toFilePath() + ".png"
        ).toString())

        if(!path.exists()) throw IllegalArgumentException("Font file $path was not found in the resource pack archive")

        val image = ImageIO.read(Files.newInputStream(path))
        val width = image.trimmedWidth()

        val scaleFactor = provider.height.toDouble() / image.height.toDouble()
        val scaledWidth = width * scaleFactor

        return FontGlyph(provider.chars.first(), provider.height, scaledWidth, provider.ascent)
    }

    @Serializable
    sealed interface FontProvider {
        @Serializable
        @SerialName("bitmap")
        class BitmapFontProvider(
            val file: IdentifiedResource,
            val ascent: Int,
            val height: Int,
            val chars: List<String>
        ) : FontProvider
    }
}