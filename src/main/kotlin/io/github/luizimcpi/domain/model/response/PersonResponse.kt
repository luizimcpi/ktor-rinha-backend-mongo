package io.github.luizimcpi.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PersonResponse(
    val uuid: String,
    val apelido: String,
    val nome: String,
    val nascimento: String,
    val stack: Set<String>? = null
)
