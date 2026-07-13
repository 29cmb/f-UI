package xyz.devcmb.fui.draw

import net.kyori.adventure.text.Component
import xyz.devcmb.fui.FontUI
import xyz.devcmb.fui.util.plus

/*
Some of this logic is used from lucyydotp who made the library that this one is based off of
https://github.com/lucyydotp/tinsel/blob/main/api/src/main/java/me/lucyydotp/tinsel/layout/BasicDrawContextImpl.java
 */

class TextDrawContext internal constructor(val fontUI: FontUI, val totalWidth: Int) {
    var cursorX: Int = 0
    var cursorY: Int = 0

    var component: Component = Component.empty()

    fun moveCursor(x: Int, y: Int) {
        if(cursorX != x) {
            component += Spacer.getSpacing(fontUI, x - cursorX)
            cursorX = x
        }
        cursorY = y
    }

    fun drawAligned(component: Component, alignment: Alignment) {
        // TODO
    }

    enum class Alignment(alignmentConstant: Float) {
        LEFT(0f),
        RIGHT(1f),
        CENTER(0f)
    }
}