package xyz.devcmb.fui.builder

import xyz.devcmb.fui.FontUI

fun buildFontUI(exec: FontUIBuilderContext.() -> Unit): FontUI {
    val context = FontUIBuilderContext()
    exec(context)
    return context.fontUI
}