package xyz.devcmb.fuiExample

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.builder.buildFontUI
import xyz.devcmb.fui.draw.Spacer
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
                for (i in 0..10) {
                    put('\uE000' + i, 1 shl i)
                }

                for (i in 0..10) {
                    put('\uF8FF' - i, -(1 shl i))
                }
            }, NamespacedKey("example", "spaces"))
        }
    }

    override fun onDisable() {
    }
}
