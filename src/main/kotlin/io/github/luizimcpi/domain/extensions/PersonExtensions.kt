package io.github.luizimcpi.domain.extensions

import io.github.luizimcpi.domain.model.entity.Person
import io.github.luizimcpi.domain.model.request.PersonRequest
import io.github.luizimcpi.resource.repository.mongo.collection.MongoPersonCollection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID


fun PersonRequest.toPerson(): Person =
    Person(
        uuid = UUID.randomUUID().toString(),
        apelido = this.apelido!!,
        nome = this.nome,
        nascimento = this.nascimento,
        stack = this.stack
    )

fun Person.toPersonCollection(): MongoPersonCollection =
    MongoPersonCollection(
        uuid = UUID.randomUUID().toString(),
        apelido = this.apelido!!,
        nome = this.nome,
        nascimento = LocalDate.parse(this.nascimento),
        stack = this.stack
    )

fun MongoPersonCollection.toPerson(): Person =
    Person(
        uuid = this.uuid,
        apelido = this.apelido,
        nome = this.nome,
        nascimento = this.nascimento.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        stack = this.stack
    )