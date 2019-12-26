package com.sanastasov.birthdaykata

import arrow.core.extensions.list.traverse.sequence
import arrow.core.fix
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.applicative.applicative
import arrow.fx.fix
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


interface EmailService {

    fun sendGreeting(emailMessage: EmailMessage): IO<Unit>

    fun sendGreetings(greetings: List<EmailMessage>): IO<List<Unit>> =
        greetings
            .map { sendGreeting(it) }
            .sequence(IO.applicative())
            .fix()
            .map { it.fix() }
}

class SmtpEmailService(private val host: String, private val port: Int) : EmailService {

    override fun sendGreeting(emailMessage: EmailMessage): IO<Unit> = IO.fx {
        val session = effect { buildSession() }.bind()
        val message = effect { createMessage(session, emailMessage) }.bind()
        IO { Transport.send(message) }.bind()
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