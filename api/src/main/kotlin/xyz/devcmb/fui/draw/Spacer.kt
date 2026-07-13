package xyz.devcmb.fui.draw

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import xyz.devcmb.fui.FontUI

object Spacer {
    data class SpacingEntry(val char: Char, val spacing: Int, val font: Key)

    fun getSpacing(fontUI: FontUI, space: Int): Component {
        if(space == 0) return Component.empty()

        var remainingSpace = space
        val allSpaces = fontUI.spaces.flatMap { (key, spaces) ->
            spaces.map { (char, value) ->
                SpacingEntry(char, value, key)
            }
        }
        var component: Component = Component.empty()

        fun search() {
            val filteredSpaces = allSpaces.filter {
                if (remainingSpace > 0) it.spacing in 1..remainingSpace
                else it.spacing in remainingSpace..-1
            }

            val entry = if (remainingSpace > 0) filteredSpaces.maxByOrNull { it.spacing }
            else filteredSpaces.minByOrNull { it.spacing }

            if(entry == null) {
                fontUI.fontUILogger.log("Could not perfectly match $space using the provided glyphs")
                return
            }

            fontUI.fontUILogger.log("Adding space glyph for ${entry.spacing} spacing")
            component = component.append(
                Component.text(entry.char).font(entry.font)
            )
            remainingSpace -= entry.spacing

            if(remainingSpace != 0) search()
        }

        search()

        return component
    }
}