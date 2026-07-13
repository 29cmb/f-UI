package xyz.devcmb.fui.font

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ObjectComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.util.DefaultFontGlyph

class FontMeasurer(val fontUI: FontUI) {
    /**
     * Measures a given string using its font glyphs
     * @param string The string to measure
     * @param bold If the string is bolded
     * @param font The font key, if not vanilla.
     */
    fun measureString(string: String, bold: Boolean = false, font: Key? = null): Double {
        if(font == null || (font.namespace() == "minecraft" && font.value() == "default")) return measureVanillaString(string, bold).toDouble()

        val fontGlyphs = fontUI.glyphs[font]
        if(fontGlyphs == null) {
            fontUI.fontUILogger.error("Could not find a font with key ${font.asMinimalString()}, returning 0 width")
            return 0.0
        }

        var length = 0.0
        string.forEach { char ->
            // spacing in between each character
            if(length != 0.0) length += 1.0

            val glyph = fontGlyphs.find { it.char == char }
            if(glyph == null) {
                fontUI.fontUILogger.warning("Character $char not found in font ${font.asMinimalString()}, returning default width of 4")
                length += 4.0
                return@forEach
            }

            length += glyph.width
            if(bold) length++
        }

        return length
    }

    /**
     * Measures a string using the default minecraft string widths
     * @param string The string to measure
     * @param bold If the string is bold
     */
    fun measureVanillaString(string: String, bold: Boolean = false): Int {
        var length = 0
        string.forEach {
            // spacing in between each character
            if(length != 0) length += 1

            val charWidth = DefaultFontGlyph.getFromChar(it).width
            length += charWidth

            if(bold) length++
        }

        return length
    }

    /**
     * Measures a component
     *
     * @param component The component to measure
     * @param parentBold If the parent of the component is bolded to be used as a fallback if the [TextDecoration.BOLD] flag is not set
     * @param contentBefore If there was any content before this component, used to add the 1-pixel spaces in between glyphs
     */
    fun measureComponent(component: Component, parentBold: Boolean = false, contentBefore: Boolean = false): Double {
        val bold = when(component.decoration(TextDecoration.BOLD)) {
            TextDecoration.State.TRUE -> true
            TextDecoration.State.FALSE -> false
            TextDecoration.State.NOT_SET -> parentBold
        }

        var length = 0.0
        when(component) {
            is TextComponent -> {
                val len = measureString(component.content(), bold, component.font())
                fontUI.fontUILogger.log("Component '${component.content()}' is $len pixels long")
                length += len
            }
            is ObjectComponent -> {
                when(val contents = component.contents()) {
                    is PlayerHeadObjectContents -> {
                        if(contentBefore) length += 1
                        length += 8
                    }
                    else -> fontUI.fontUILogger.warning("Unknown object component type ${contents::class.qualifiedName}, skipping")
                }
            }
            else -> {
                fontUI.fontUILogger.warning("Unknown component type ${component::class.qualifiedName}, skipping")
            }
        }

        if(length != 0.0 && contentBefore) length += 1

        component.children().forEach {
            length += measureComponent(it, bold, length != 0.0)
        }

        return length
    }
}