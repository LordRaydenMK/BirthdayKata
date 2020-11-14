package com.sanastasov.birthdaykata

import java.time.LocalDate

interface Env : EmployeeRepository,
    BirthdayService,
    EmailService

suspend fun main() {
    val env: Env = object : Env,
        EmployeeRepository by FileEmployeeRepository("input.txt"),
        BirthdayService by BirthdayServiceInterpreter(),
        EmailService by SmtpEmailService("localhost", 8080) {}

    env.sendGreetingsUseCase(date = LocalDate.now())
}

suspend fun Env.sendGreetingsUseCase(date: LocalDate): Unit {
    val allEmployees = allEmployees()
    val greetings = birthdayMessages(allEmployees, date)
    sendGreetings(greetings)
    Unit
}