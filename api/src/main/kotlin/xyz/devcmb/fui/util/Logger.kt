package xyz.devcmb.fui.util

import xyz.devcmb.fui.builder.FontUIBuilderContext
import java.util.UUID
import java.util.logging.Logger

class Logger(val builder: FontUIBuilderContext) {
    private val javaLogger = Logger.getLogger("FUI/${UUID.randomUUID().toString().take(5)}")
    fun log(msg: String) {
        if(builder.debug) javaLogger.info(msg)
    }
}