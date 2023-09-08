package io.github.luizimcpi.web.validation

import io.github.luizimcpi.domain.model.request.PersonRequest
import io.github.luizimcpi.domain.model.response.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<PersonRequest> { personRequest ->
            if (personRequest.apelido == null)
                ValidationResult.Invalid("apelido must not be null")
            else if (personRequest.apelido != null && personRequest.apelido!!.length > 32)
                ValidationResult.Invalid("apelido must contain a maximum of 32 chars")
            else ValidationResult.Valid
        }
    }
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.UnprocessableEntity, ErrorResponse(cause.reasons.joinToString()))
        }
    }
}