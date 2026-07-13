package xyz.devcmb.fui.font

/**
 * A single font glyph apart of a font
 */
@ConsistentCopyVisibility
data class FontGlyph internal constructor(
    /** The character that represents this glyph */
    val char: Char,
    /** The height of the glyph's texture, including emtpy pixels */
    val height: Int,
    /** The width of how the glyph will be rendered, accounting for the value of [height] and excluding empty pixels */
    val width: Double,
    /** The ascent value of this glyph */
    val ascent: Int
)