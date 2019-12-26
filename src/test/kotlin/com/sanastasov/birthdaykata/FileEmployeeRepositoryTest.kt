package com.sanastasov.birthdaykata

import com.sanastasov.birthdaykata.FileEmployeeRepository.EmployeeRepositoryException
import io.kotlintest.specs.StringSpec
import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows

/**
 * Integration tests, touches the file system
 */
class FileEmployeeRepositoryTest : StringSpec({

    "all employees are read from a valid CSV file" {
        val sut: EmployeeRepository = FileEmployeeRepository("input.txt")

        val allEmployees = sut.allEmployees().unsafeRunSync()

        val expectedEmails = listOf("john.doe@foobar.com", "mary.ann@foobar.com").map(::EmailAddress)
        allEmployees.size shouldBe 2
        allEmployees.map { it.emailAddress } shouldBe expectedEmails
    }

    "EmployeeRepositoryException when reading an invalid CSV file" {
        val sut: EmployeeRepository = FileEmployeeRepository("invalid_csv_input.txt")

        val exception = assertThrows<EmployeeRepositoryException> {
            sut.allEmployees().unsafeRunSync()
        }
        exception.errors.size shouldBe 4
    }
})