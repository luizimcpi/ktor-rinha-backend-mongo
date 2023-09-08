package io.github.luizimcpi.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String) {
    companion object {
        val PERSON_NOT_FOUND_RESPONSE = ErrorResponse("Pessoa não encontrada")
        val BAD_REQUEST_RESPONSE = ErrorResponse("Invalid request")
    }
}