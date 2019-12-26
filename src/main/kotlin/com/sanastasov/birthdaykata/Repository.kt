package com.sanastasov.birthdaykata

import arrow.core.Nel
import arrow.core.extensions.list.traverse.sequence
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import arrow.fx.IO
import arrow.fx.extensions.fx
import java.io.BufferedReader
import java.io.File

interface EmployeeRepository {

    fun allEmployees(): IO<List<Employee>>
}

class FileEmployeeRepository(fileName: String) : EmployeeRepository {

    private val file = File(fileName)

    override fun allEmployees(): IO<List<Employee>> =
        IO { file.bufferedReader() }.bracket(
            release = { IO { it.close() } },
            use = readFile()
        )

    private fun readFile(): (BufferedReader) -> IO<List<Employee>> = { br: BufferedReader ->
        IO.fx {
            val employees = IO {
                br.readLines()
                    .drop(1)
                    .map(employeeParser)
            }.bind()

            val validatedEmployees = sequence(employees)

            validatedEmployees.fold(
                { IO.raiseError<List<Employee>>(EmployeeRepositoryException(it)) },
                { IO.just(it) }
            ).bind()
        }
    }

    private fun sequence(input: List<ValidationResult<Employee>>): ValidationResult<List<Employee>> =
        input.sequence(ValidationResult.applicative(Nel.semigroup<String>()))
            .fix()
            .map { it.fix() }

    data class EmployeeRepositoryException(
        val errors: Nel<String>
    ) : RuntimeException("Error reading from repo: $errors")

    companion object {

        val employeeParser: (String) -> ValidationResult<Employee> = { row ->
            val parts = row.split(", ")
            val lastName = parts.getOrNull(0)
            val firstName = parts.getOrNull(1)
            val dateOfBirth = parts.getOrNull(2)
            val email = parts.getOrNull(3)
            Employee(firstName, lastName, dateOfBirth, email)
        }
    }
}