package com.sanastasov.birthdaykata

import arrow.core.identity
import arrow.core.valid
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.LocalDate

class EmployeeParserTest : StringSpec({

    "parses a valid employee for correct CSV row" {
        val row = "Doe, John, 1982/10/08, john.doe@foobar.com"

        val employee = FileEmployeeRepository.employeeParser(row)

        employee.isValid shouldBe true
        employee shouldBe Employee(
            "John",
            "Doe",
            LocalDate.of(1982, 10, 8),
            EmailAddress("john.doe@foobar.com")
        ).valid()
    }

    "reports all errors for invalid CSV row" {
        val row = ", , 2, fakeemail"

        val employee = FileEmployeeRepository.employeeParser(row)

        employee.isInvalid shouldBe true
        val errors = employee.fold(::identity) { throw IllegalStateException() }
        println(errors)
        errors.contains("Email must contain '@' found: 'fakeemail'") shouldBe true
        errors.contains("Invalid date found: '2'") shouldBe true
        errors.contains("Name must not be blank, found ''") shouldBe true
        errors.contains("Name must not be blank, found ''") shouldBe true
    }
})