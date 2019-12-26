package com.sanastasov.birthdaykata

import arrow.fx.IO
import arrow.fx.handleErrorWith
import mu.KotlinLogging

interface LoggingService {

    fun logError(t: Throwable, msg: () -> String?): IO<Unit>
}

class KotlinLoggingLogger : LoggingService {

    private val logger = KotlinLogging.logger("AppLogger")

    override fun logError(t: Throwable, msg: () -> String?): IO<Unit> = IO { logger.error(t, msg) }
}

fun <A> IO<A>.logError(log: (Throwable, () -> String?) -> IO<Unit>, msg: () -> String? = { null }): IO<A> =
    handleErrorWith { log(it, msg).followedBy(IO.raiseError(it)) }