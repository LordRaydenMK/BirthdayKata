package com.sanastasov.birthdaykata.fakes

import arrow.fx.IO
import com.sanastasov.birthdaykata.LoggingService

class TestLogger : LoggingService {

    override fun logError(t: Throwable, msg: () -> String?): IO<Unit> = IO.unit // no-op
}