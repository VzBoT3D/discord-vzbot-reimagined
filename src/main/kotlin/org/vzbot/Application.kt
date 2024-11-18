package org.vzbot

import com.zellerfeld.zellerbotapi.ZellerBot
import com.zellerfeld.zellerbotapi.util.Token
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.vzbot.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureMiddlewares()
    configureRouting()
    configureBot()
}
