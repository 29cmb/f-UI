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
    lateinit var fUI: FontUI

    override fun onEnable() {
        saveResource("pack.zip", true)

        fUI = buildFontUI(File(this.dataFolder, "pack.zip")) {
            debug = true

            registerFont(NamespacedKey("example", "glyphs"))
            registerDefaultAscents(-4, 4, -8, 8, -12, 12, -16, 16, -20, 20, -24, 24, -28, 28, -32, 32, -36, 36, -40, 40, 44, -44, 48, -48) {
                NamespacedKey("example", "default_offset_$it")
            }

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
                it.sendActionBar(fUI.draw(120) { ctx ->
                    ctx.drawAligned(mm("<font:example:glyphs>\uF001</font>").shadowColor(ShadowColor.shadowColor(0)), TextDrawContext.Alignment.CENTER)
                    ctx.drawAligned(mm("<white>This is an <gold><b>example</b></gold>"), TextDrawContext.Alignment.CENTER)

                    ctx.moveCursor(ctx.cursorX, -8)
                    ctx.drawAligned(mm("<green>Wow!</green>"), TextDrawContext.Alignment.CENTER)

                    ctx.moveCursor(-60, -8)
                    ctx.draw(mm("<white><b>Header 1</b></white>"), 0.0)
                    ctx.moveCursor(-60, 0)
                    ctx.draw(mm("<gold>9174452</gold>"), 0.0)

                    ctx.moveCursor(172, -8)
                    ctx.draw(mm("<white><b>Header 2</b></white>"), 1.0)
                    ctx.moveCursor(172, 0)
                    ctx.draw(mm("<aqua>7291534</aqua>"), 1.0)
                })
            }
        }, 0, 20)
        fUI.spacer.getSpacing(1.25)
    }

    override fun onDisable() {
    }

    val miniMessage = MiniMessage.miniMessage()
    fun mm(string: String): Component = miniMessage.deserialize(string)
}
