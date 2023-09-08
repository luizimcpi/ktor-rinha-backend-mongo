package io.github.luizimcpi.web.validation

import io.github.luizimcpi.domain.model.request.PersonRequest
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

fun RequestValidationConfig.personRequestValidations() {
    validate<PersonRequest> { personRequest ->
        if (personRequest.apelido.isNullOrEmpty())
            ValidationResult.Invalid("provide a value for field apelido")
        else if (personRequest.apelido!!.length > 32)
            ValidationResult.Invalid("apelido must contain a maximum of 32 chars")
        else if (personRequest.nome.isNullOrEmpty())
            ValidationResult.Invalid("provide a value for field nome")
        else if (personRequest.nome!!.length > 100)
            ValidationResult.Invalid("nome must contain a maximum of 100 chars")
        else ValidationResult.Valid
    }
}