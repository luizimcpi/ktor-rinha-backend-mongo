package io.github.luizimcpi.configuration

import io.github.luizimcpi.web.personRouting
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(mongoUrl: String) {
    routing {
        get("/") {
            call.respondText("Service Status UP")
        }
        personRouting(mongoUrl)
    }
}
