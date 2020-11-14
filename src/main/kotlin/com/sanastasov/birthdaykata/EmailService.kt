package com.sanastasov.birthdaykata

import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


interface EmailService {

    suspend fun sendGreeting(emailMessage: EmailMessage): Unit

    suspend fun sendGreetings(greetings: List<EmailMessage>): List<Unit> =
        greetings.map { sendGreeting(it) }
}

class SmtpEmailService(private val host: String, private val port: Int) : EmailService {

    override suspend fun sendGreeting(emailMessage: EmailMessage): Unit {
        val session =  buildSession()
        val message = createMessage(session, emailMessage)
        Transport.send(message)
    }

    private suspend fun buildSession(): Session {
        val props = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", port.toString())
        }

        return Session.getInstance(props, null)
    }

    private suspend fun createMessage(session: Session, emailMessage: EmailMessage): Message =
        MimeMessage(session).apply {
            setFrom(emailMessage.from.toInternetAddress())
            setRecipient(Message.RecipientType.TO, emailMessage.to.toInternetAddress())
            subject = emailMessage.subject
            setText(emailMessage.message)
        }

    private fun EmailAddress.toInternetAddress() = InternetAddress(email)
}