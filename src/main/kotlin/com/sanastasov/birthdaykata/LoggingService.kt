package com.sanastasov.birthdaykata

import arrow.fx.IO
import arrow.fx.handleErrorWith
import mu.KotlinLogging

interface LoggingService {

    fun logError(t: Throwable, msg: () -> String?): IO<Unit>

    fun <A> IO<A>.logError(msg: (Throwable) -> String?) =
        handleErrorWith { logError(it) { msg(it) }.followedBy(IO.raiseError(it)) }
}

class KotlinLoggingLogger : LoggingService {

    private val logger = KotlinLogging.logger("AppLogger")

    override fun logError(t: Throwable, msg: () -> String?): IO<Unit> = IO { logger.error(t, msg) }
}