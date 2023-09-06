package io.github.luizimcpi.domain.model.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Person(
    @BsonId
    val id: Id<Person>? = null,
    val uuid: String,
    val apelido: String,
    val nome: String,
    val nascimento: String, //AAAA-MM-DD
    val stack: Set<String>? = null
)