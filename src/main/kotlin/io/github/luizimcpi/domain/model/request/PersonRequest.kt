package io.github.luizimcpi.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PersonRequest(
    val apelido: String? = null,
    val nome: String? = null,
    val nascimento: String? = null,
    val stack: Set<String>? = null
)
