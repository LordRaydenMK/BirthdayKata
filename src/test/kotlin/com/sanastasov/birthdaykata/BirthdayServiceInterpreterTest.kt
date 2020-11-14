package com.sanastasov.birthdaykata


import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month

class BirthdayServiceInterpreterTest : StringSpec() {

    val sut: BirthdayService = BirthdayServiceInterpreter()

    init {
        "returns messages for multiple employees born on the same day" {
            val messages = sut.birthdayMessages(testData(), LocalDate.of(2020, 10, 10))

            messages.size shouldBe 2
            messages.map { it.to } shouldBe listOf("arso@foobar.com", "pero@foobar.com").map(::EmailAddress)
        }

        "returns empty list when there is no birthday" {
            val messages = sut.birthdayMessages(testData(), LocalDate.of(2020, 1, 1))

            messages.size shouldBe 0
        }

        "email message is in the right format" {
            val messages = sut.birthdayMessages(testData(), LocalDate.of(2020, 5, 5))

            messages.first() shouldBe EmailMessage(
                EmailAddress("birthday@corp.com"),
                EmailAddress("tosho@foobar.com"),
                "Happy Birthday!",
                "Happy birthday, dear Toshe!"
            )
        }

        "sends email to people born on Feb 29 on Feb 28 when non leap year" {
            val messages = sut.birthdayMessages(testData(), LocalDate.of(2021, Month.FEBRUARY, 28))

            messages.size shouldBe 2
        }

        "sends email only to people born on Feb 28 when leap year" {
            val messages = sut.birthdayMessages(testData(), LocalDate.of(2020, Month.FEBRUARY, 28))

            println(messages)
            messages.size shouldBe 1
        }
    }

    private fun testData(): List<Employee> =
        listOf(
            Employee("Arso", "Arsov", LocalDate.of(1990, 10, 10), EmailAddress("arso@foobar.com")),
            Employee("Petar", "Petrov", LocalDate.of(1980, 10, 10), EmailAddress("pero@foobar.com")),
            Employee("Toshe", "Toshev", LocalDate.of(1997, 5, 5), EmailAddress("tosho@foobar.com")),
            Employee("John", "Doe", LocalDate.of(1991, 2, 28), EmailAddress("jd17@foobar.com")),
            Employee("Jane", "Doe", LocalDate.of(1992, 2, 29), EmailAddress("jd22@foobar.com"))
        )
}