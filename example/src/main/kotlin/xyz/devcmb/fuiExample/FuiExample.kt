package xyz.devcmb.fuiExample

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.builder.buildFontUI
import java.io.File

class FuiExample : JavaPlugin() {
    lateinit var fUI: FontUI

    override fun onEnable() {
        saveResource("pack.zip", true)

        fUI = buildFontUI(File(this.dataFolder, "pack.zip")) {
            debug = true
            registerFont(NamespacedKey("example", "glyphs"))
        }
    }

    override fun onDisable() {
    }
}
