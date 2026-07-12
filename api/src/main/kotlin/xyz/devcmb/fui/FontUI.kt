package xyz.devcmb.fui

import net.kyori.adventure.key.Key
import xyz.devcmb.fui.font.DefaultAscentFont
import xyz.devcmb.fui.font.FontGlyph

class FontUI internal constructor(val glyphs: HashMap<Key, ArrayList<FontGlyph>>, val defaultFontAscents: ArrayList<DefaultAscentFont>) {
}