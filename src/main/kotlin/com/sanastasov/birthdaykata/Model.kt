package com.sanastasov.birthdaykata

import arrow.core.Nel
import arrow.core.ValidatedNel
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import arrow.core.invalidNel
import arrow.core.valid
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

typealias ValidationResult<A> = ValidatedNel<String, A>

data class EmailAddress(val email: String) {

    companion object {

        operator fun invoke(email: String?): ValidationResult<EmailAddress> =
            if (email != null && email.contains("@")) EmailAddress(email).valid()
            else "Email must contain '@' found: '$email'".invalidNel()
    }
}

data class Employee(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val emailAddress: EmailAddress
) {

    companion object {

        private val DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu/MM/dd")

        private fun validateName(value: String?): ValidationResult<String> =
            if (!value.isNullOrBlank()) value.valid()
            else "Name must not be blank, found '$value'".invalidNel()

        private fun validateDateOfBirth(dob: String?): ValidationResult<LocalDate> =
            try {
                if (dob == null) "Date of birth can't be empty. Found '$dob'".invalidNel()
                else LocalDate.parse(dob, DATE_FORMAT).valid()
            } catch (e: DateTimeParseException) {
                "Invalid date found: '$dob'".invalidNel()
            }

        operator fun invoke(
            firstName: String?,
            lastName: String?,
            dateOfBirth: String?,
            email: String?
        ): ValidationResult<Employee> =
            ValidationResult.applicative(Nel.semigroup<String>())
                .tupledN(
                    validateName(firstName),
                    validateName(lastName),
                    validateDateOfBirth(dateOfBirth),
                    EmailAddress(email)
                ).fix()
                .map { (fn, ln, dob, e) -> Employee(fn, ln, dob, e) }
    }
}

data class EmailMessage(
    val from: EmailAddress,
    val to: EmailAddress,
    val subject: String,
    val message: String
)