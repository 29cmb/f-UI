package xyz.devcmb.fui

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import xyz.devcmb.fui.draw.TextDrawContext
import xyz.devcmb.fui.font.DefaultAscentFont
import xyz.devcmb.fui.font.FontGlyph
import xyz.devcmb.fui.util.FontUILogger

class FontUI internal constructor(
    val glyphs: HashMap<Key, ArrayList<FontGlyph>>,
    val defaultFontAscents: ArrayList<DefaultAscentFont>,
    val spaces: HashMap<Key, Map<Char, Int>>,
    val fontUILogger: FontUILogger
) {
    fun draw(totalWidth: Int, run: TextDrawContext.() -> Unit): Component {
        val context = TextDrawContext(this, totalWidth)
        context.run()
        return context.component
    }
}