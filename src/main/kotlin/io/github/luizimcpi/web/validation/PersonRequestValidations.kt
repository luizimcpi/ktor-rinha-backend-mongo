package io.github.luizimcpi.web.validation

import io.github.luizimcpi.domain.model.request.PersonRequest
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.time.LocalDate

fun RequestValidationConfig.personRequestValidations() {
    validate<PersonRequest> { personRequest ->
        if (personRequest.apelido.isNullOrEmpty())
            ValidationResult.Invalid("Campo 'apelido' é obrigatório")
        else if(isNumber(personRequest.apelido!!))
            throw BadRequestException("Campo 'apelido' deve ser uma string")
        else if (personRequest.apelido!!.length > 32)
            ValidationResult.Invalid("Campo 'apelido' deve ter no máximo 32 caracteres")
        else if (personRequest.nome.isNullOrEmpty())
            ValidationResult.Invalid("Campo 'nome' é obrigatório")
        else if(isNumber(personRequest.nome!!))
            throw BadRequestException("Campo 'nome' deve ser uma string")
        else if (personRequest.nome!!.length > 100)
            ValidationResult.Invalid("Campo 'nome' deve ter no máximo 100 caracteres")
        else if (personRequest.nascimento.isNullOrEmpty())
            ValidationResult.Invalid("Campo 'nascimento' é obrigatório")
        else if(isNumber(personRequest.nascimento!!))
            throw BadRequestException("Campo 'nascimento' deve ser uma string")
        else if(invalidDate(personRequest.nascimento!!))
            ValidationResult.Invalid("Campo 'nascimento' deve seguir o formato 'AAAA-MM-DD'")
        else if(!personRequest.stack.isNullOrEmpty() && stackHasNumber(personRequest.stack))
            throw BadRequestException("Campo 'stack' deve conter apenas strings")
        else ValidationResult.Valid
    }
}

fun stackHasNumber(stack: Set<String>): Boolean {
    for (item in stack) {
        if(isNumber(item)) return true
    }
    return false
}

fun invalidDate(nascimento: String): Boolean {
    try {
        LocalDate.parse(nascimento)
        return false
    } catch (e: Exception){
        return true
    }
}

fun isNumber(field: String): Boolean {
    try {
        field.toDouble()
        return true
    } catch (e: NumberFormatException) {
        return false
    }
}