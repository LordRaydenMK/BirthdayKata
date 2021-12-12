package com.sanastasov.birthdaykata

import arrow.core.Nel
import arrow.core.identity
import arrow.core.sequenceValidated
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
                .sequenceValidated()

        employees.fold({ throw EmployeeRepositoryException(it) }, ::identity)
    }

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