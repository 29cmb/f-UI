package xyz.devcmb.fui.draw

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import xyz.devcmb.fui.FontUI
import kotlin.math.abs

class Spacer(val fontUI: FontUI) {
    data class SpacingEntry(val char: Char, val spacing: Double, val font: Key)

    /**
     * Gets the spacing glyphs to negative or positive space a set amount
     * @param space The space that is needed
     */
    fun getSpacing(space: Double): Component {
        if(space == 0.0) return Component.empty()

        var remainingSpace = space
        val allSpaces = fontUI.spaces.flatMap { (key, spaces) ->
            spaces.map { (char, value) ->
                SpacingEntry(char, value, key)
            }
        }
        var component: Component = Component.empty()

        fun search() {
            val filteredSpaces = allSpaces.filter {
                if (remainingSpace > 0.0) it.spacing > 0.0 && it.spacing <= remainingSpace
                else it.spacing in remainingSpace..<0.0
            }

            val entry = if (remainingSpace > 0) filteredSpaces.maxByOrNull { it.spacing }
            else filteredSpaces.minByOrNull { it.spacing }

            if(entry == null) {
                fontUI.fontUILogger.warning("Could not perfectly match $space using the provided glyphs")
                return
            }

            fontUI.fontUILogger.log("Adding space glyph for ${entry.spacing} spacing")
            component = component.append(
                Component.text(entry.char).font(entry.font)
            )
            remainingSpace -= entry.spacing

            if(abs(remainingSpace) > 1e-5) search()
        }

        search()

        return component
    }
}