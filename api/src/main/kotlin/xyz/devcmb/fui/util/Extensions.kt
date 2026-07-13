package xyz.devcmb.fui.util

import net.kyori.adventure.text.Component
import java.awt.image.BufferedImage

internal fun BufferedImage.trimmedWidth(): Int {
    var left = 0
    var right = width - 1

    while (left <= right && isColumnEmpty(left)) {
        left++
    }

    while (right >= left && isColumnEmpty(right)) {
        right--
    }

    return if (left > right) 0 else right - left + 1
}

private fun BufferedImage.isColumnEmpty(x: Int): Boolean {
    for (y in 0 until height) {
        val alpha = getRGB(x, y) ushr 24 and 0xFF
        if (alpha != 0) {
            return false
        }
    }
    return true
}

internal operator fun Component.plus(other: Component): Component = this.append(other)