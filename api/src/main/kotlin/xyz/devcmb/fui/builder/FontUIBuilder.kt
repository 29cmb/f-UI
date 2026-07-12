package xyz.devcmb.fui.builder

import xyz.devcmb.fui.FontUI
import java.io.File

/** Constructs a [FontUIBuilderContext] with the resource pack zip [pack] **/
fun buildFontUI(pack: File, exec: FontUIBuilderContext.() -> Unit): FontUI {
    if(!pack.exists() || pack.extension != "zip") throw IllegalArgumentException("Pack file must be a valid zip archive")

    val context = FontUIBuilderContext(pack)
    exec(context)
    return context.build()
}