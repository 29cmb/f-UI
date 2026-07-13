package xyz.devcmb.fui.util

import xyz.devcmb.fui.builder.FontUIBuilderContext
import java.util.UUID
import java.util.logging.Logger

class FontUILogger internal constructor(val builder: FontUIBuilderContext) {
    private val javaLogger = Logger.getLogger("FUI/${UUID.randomUUID().toString().take(5)}")
    fun log(msg: String, override: Boolean = false) {
        if(builder.debug || override) javaLogger.info(msg)
    }

    fun warning(msg: String, override: Boolean = false) {
        if(builder.debug || override) javaLogger.warning(msg)
    }

    fun error(msg: String) {
        if (builder.debug) javaLogger.severe(msg)
    }
}