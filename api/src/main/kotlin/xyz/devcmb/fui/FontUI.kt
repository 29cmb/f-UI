package xyz.devcmb.fui

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import xyz.devcmb.fui.draw.Spacer
import xyz.devcmb.fui.draw.TextDrawContext
import xyz.devcmb.fui.font.DefaultAscentFont
import xyz.devcmb.fui.font.FontGlyph
import xyz.devcmb.fui.font.FontMeasurer
import xyz.devcmb.fui.util.FontUILogger

class FontUI internal constructor(
    val glyphs: HashMap<Key, ArrayList<FontGlyph>>,
    val defaultFontAscents: ArrayList<DefaultAscentFont>,
    val spaces: HashMap<Key, Map<Char, Double>>,
    val fontUILogger: FontUILogger
) {
    val spacer = Spacer(this)
    val fontMeasurer = FontMeasurer(this)

    fun draw(totalWidth: Double, run: (ctx: TextDrawContext) -> Unit): Component {
        val context = TextDrawContext(this, totalWidth)
        run.invoke(context)
        return context.build()
    }

    fun draw(totalWidth: Int, run: (ctx: TextDrawContext) -> Unit) = draw(totalWidth.toDouble(), run)
}