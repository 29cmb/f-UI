package xyz.devcmb.fuiExample

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.builder.buildFontUI
import xyz.devcmb.fui.draw.TextDrawContext
import java.io.File

class FuiExample : JavaPlugin() {
    lateinit var fontUI: FontUI

    override fun onEnable() {
        // All files of this resource pack can be found in the `example/src/main/resources` directory
        saveResource("pack.zip", true)

        // You need to provide a copy of your resource pack that the library can access
        // The best place to put this is in your plugins data folder
        fontUI = buildFontUI(File(this.dataFolder, "pack.zip")) {
            // Enable if you want A LOT of logging
            // debug = true

            // Registers a single font
            // This will search through all the bitmap fonts with a single character and calculate their width and height
            // At this time, full font atlasses are not supported, only single character bitmaps
            registerFont(NamespacedKey("example", "glyphs"))

            // Registers default ascent fonts
            // These are copies of the vanilla font but with an additional provided ascent value
            // Then, the font key is determined by this lambda call, with the ascent value passed in
            registerDefaultAscents(-4, 4, -8, 8, -12, 12, -16, 16, -20, 20, -24, 24, -28, 28, -32, 32, -36, 36, -40, 40, 44, -44, 48, -48) {
                NamespacedKey("example", "default_offset_$it")
            }

            // Registers spacing characters
            // For this library to work properly, every combination of integers must be possible in both positive and negative.
            // The best way to do this is with spaces that shift by a power of 2
            // Decimal values are possible and can help with positioning, but be wary of floating point inaccuracies
            registerSpacingCharacters(buildMap {
                put('\uE000', 0.5)
                for (i in 1..11) {
                    put('\uE000' + i, (1 shl (i - 1)).toDouble())
                }

                put('\uF000', -0.5)
                for (i in 1..11) {
                    put('\uF000' + i, -(1 shl (i - 1)).toDouble())
                }
            }, NamespacedKey("example", "spaces"))
        }

        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                it.sendActionBar(fontUI.draw(120) { ctx ->
                    // In this space, you have access to a few methods for rendering text
                    // This renders using a cursor X and Y. X is determined by spacing, and Y by ascent fonts
                    // Each of these methods moves the X cursor in some way or another, usually to the end of the text its rendered

                    // Draws the background glyph at the center of the view with no shadow
                    ctx.drawAligned(mm("<font:example:glyphs>\uF001</font>").shadowColor(ShadowColor.shadowColor(0)), TextDrawContext.Alignment.CENTER)

                    // Draws some text centered at the default Y height
                    ctx.drawAligned(mm("<white>This is an <gold><b>example</b></gold>"), TextDrawContext.Alignment.CENTER)

                    // Moves the cursor up to render text directly above the previous centered text
                    // Lower ascents means higher text
                    ctx.moveCursor(ctx.cursorX, -8)
                    ctx.drawAligned(mm("<green>Wow!</green>"), TextDrawContext.Alignment.CENTER)

                    // Moves the cursor to the left and up to display a left aligned header
                    // This acts as normal text, moving the cursor forward
                    ctx.moveCursor(-60, -8)
                    ctx.draw(mm("<white><b>Header 1</b></white>"), TextDrawContext.Alignment.LEFT)

                    // Moves the cursor back down to render a left aligned number
                    ctx.moveCursor(-60, 0)
                    ctx.draw(mm("<gold>9174452</gold>"), TextDrawContext.Alignment.LEFT)

                    // Does the same thing on the other side, but this time right aligned
                    // This text keeps the cursor in the same place but moves the text back
                    ctx.moveCursor(172, -8)
                    ctx.draw(mm("<white><b>Header 2</b></white>"), TextDrawContext.Alignment.RIGHT)
                    ctx.moveCursor(172, 0)
                    ctx.draw(mm("<aqua>7291534</aqua>"), TextDrawContext.Alignment.RIGHT)
                })
            }
        }, 0, 20)
    }

    val miniMessage = MiniMessage.miniMessage()
    fun mm(string: String): Component = miniMessage.deserialize(string)
}
