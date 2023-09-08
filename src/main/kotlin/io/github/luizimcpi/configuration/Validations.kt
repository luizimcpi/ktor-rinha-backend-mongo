package io.github.luizimcpi.configuration

import io.github.luizimcpi.web.validation.personRequestValidations
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureValidation() {
    install(RequestValidation) {
        personRequestValidations()
    }
}