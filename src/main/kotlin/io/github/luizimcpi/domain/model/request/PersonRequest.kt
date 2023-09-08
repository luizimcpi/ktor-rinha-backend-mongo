package io.github.luizimcpi.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonRequest(
    val apelido: String? = null,
    val nome: String,
    val nascimento: String, //AAAA-MM-DD
    val stack: Set<String>? = null
)
