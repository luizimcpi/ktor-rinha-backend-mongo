package io.github.luizimcpi.domain.model.entity

data class Person(
    val uuid: String,
    val apelido: String,
    val nome: String,
    val nascimento: String,
    val stack: Set<String>? = null
)