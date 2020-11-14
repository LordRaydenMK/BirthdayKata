package com.sanastasov.birthdaykata

import com.dumbster.smtp.SimpleSmtpServer
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import java.time.LocalDate

/**
 * Integration test, touches the file system and uses a mock SMTP server
 */
class AcceptanceTest : StringSpec() {

    lateinit var server: SimpleSmtpServer

    fun testEnv(port: Int): Env = object : Env,
        EmployeeRepository by FileEmployeeRepository("input.txt"),
        BirthdayService by BirthdayServiceInterpreter(),
        EmailService by SmtpEmailService("localhost", port) {}

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        server.stop()
        super.afterTest(testCase, result)
    }

    init {
        "will send greeting when it is somebody birthday" {
            testEnv(server.port)
                .sendGreetingsUseCase(LocalDate.of(2019, 10, 8))

            val sent = server.receivedEmails.toList()

            sent.size shouldBe 1
            val email = sent.first()
            email.getHeaderValue("From") shouldBe "birthday@corp.com"
            email.getHeaderValue("To") shouldBe "john.doe@foobar.com"
            email.getHeaderValue("Subject") shouldBe "Happy Birthday!"
            email.body shouldBe "Happy birthday, dear John!"
        }

        "will not send greeting when there is no birthday" {
            testEnv(server.port)
                .sendGreetingsUseCase(LocalDate.of(2019, 12, 1))

            server.receivedEmails.toList().size shouldBe 0
        }
    }
}