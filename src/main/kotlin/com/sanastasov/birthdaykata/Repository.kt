package com.sanastasov.birthdaykata

import arrow.core.Nel
import arrow.core.extensions.list.traverse.sequence
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import arrow.core.fix
import arrow.core.identity
import arrow.fx.coroutines.bracket
import java.io.BufferedReader
import java.io.File

interface EmployeeRepository {

    suspend fun allEmployees(): List<Employee>
}

class FileEmployeeRepository(fileName: String) : EmployeeRepository {

    private val file = File(fileName)

    override suspend fun allEmployees(): List<Employee> =
        bracket({ file.bufferedReader() }, readFile(), { it.close() })

    private fun readFile(): suspend (BufferedReader) -> List<Employee> = { br: BufferedReader ->
        val employees =
            br.readLines()
                .drop(1)
                .map(employeeParser)

        val validatedEmployees = sequence(employees)

        validatedEmployees.fold({ throw EmployeeRepositoryException(it) }, ::identity)
    }

    private fun sequence(input: List<ValidationResult<Employee>>): ValidationResult<List<Employee>> =
        input.sequence(ValidationResult.applicative(Nel.semigroup()))
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