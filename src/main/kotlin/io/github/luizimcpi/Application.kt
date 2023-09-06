package io.github.luizimcpi

import io.github.luizimcpi.configuration.configureRouting
import io.github.luizimcpi.configuration.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost"
    configureRouting(mongoUrl)
    configureSerialization()
}
