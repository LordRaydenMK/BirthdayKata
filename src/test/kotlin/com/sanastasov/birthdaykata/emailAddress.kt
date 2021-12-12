package com.sanastasov.birthdaykata

import arrow.core.identity

fun EmailAddress.Companion.unsafeCreate(email: String): EmailAddress =
    EmailAddress(email).fold({throw IllegalArgumentException("Invalid value: $email")}, ::identity)