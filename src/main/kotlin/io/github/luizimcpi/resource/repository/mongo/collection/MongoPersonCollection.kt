package io.github.luizimcpi.resource.repository.mongo.collection

import io.github.luizimcpi.domain.model.entity.Person
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.LocalDate

data class MongoPersonCollection(
    @BsonId
    val id: Id<Person>? = null,
    val uuid: String,
    val apelido: String,
    val nome: String,
    val nascimento: LocalDate,
    val stack: Set<String>? = null
)
