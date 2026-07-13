package xyz.devcmb.fui.draw

import net.kyori.adventure.text.Component
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.util.plus
import kotlin.math.roundToInt

/*
A lot of this logic is used from lucyydotp who made the library that this one is based off of
https://github.com/lucyydotp/tinsel/blob/main/api/src/main/java/me/lucyydotp/tinsel/layout/BasicDrawContextImpl.java
 */

class TextDrawContext internal constructor(internal val fontUI: FontUI, val totalWidth: Double) {
    var cursorX: Double = 0.0; private set
    var cursorY: Int = 0; private set

    var component: Component = Component.empty(); private set

    fun moveCursor(x: Double, y: Int) {
        if(cursorX != x) {
            component += fontUI.spacer.getSpacing(x - cursorX)
            cursorX = x
        }
        cursorY = y
    }

    fun moveCursor(x: Int, y: Int) = moveCursor(x.toDouble(), y)

    fun drawAlignedWithWidth(component: Component, width: Double, alignmentConstant: Double) {
        val targetX = ((totalWidth - width) * alignmentConstant).roundToInt().toDouble()
        if(targetX != cursorX) {
            this.component += fontUI.spacer.getSpacing(targetX - cursorX)
        }
        this.component += alignY(component)
        cursorX = targetX + width
    }

    fun draw(component: Component, alignmentConstant: Double) {
        val width = fontUI.fontMeasurer.measureComponent(component)
        moveCursor(cursorX + (-(width * alignmentConstant).roundToInt()), cursorY)
        drawWithWidth(component, width)
    }

    fun draw(component: Component, alignment: Alignment) = draw(component, alignment.alignmentConstant)

    fun drawWithWidth(component: Component, width: Double) {
        this.component += alignY(component)
        cursorX += width
    }

    fun drawAligned(component: Component, alignmentConstant: Double) =
        drawAlignedWithWidth(component, fontUI.fontMeasurer.measureComponent(component), alignmentConstant)

    fun drawAligned(component: Component, alignment: Alignment) = drawAligned(component, alignment.alignmentConstant)

    internal fun build(): Component {
        if(cursorX != totalWidth) {
            component += fontUI.spacer.getSpacing(totalWidth - cursorX)
        }

        return component
    }

    internal fun alignY(component: Component): Component {
        if(component.font() != null && (component.font()?.namespace() != "minecraft" || component.font()?.value() != "default")) return component
        if(cursorY == 0) return component

        val defaultAscentFonts = fontUI.defaultFontAscents
        val closest = defaultAscentFonts
            .filter { it.ascent <= cursorY && (cursorY > 0) == (it.ascent > 0) }
            .maxByOrNull { it.ascent } ?: return component

        fontUI.fontUILogger.log(closest.resource.toString())
        return component.font(closest.resource)
    }

    enum class Alignment(val alignmentConstant: Double) {
        LEFT(0.0),
        RIGHT(1.0),
        CENTER(0.5)
    }
}