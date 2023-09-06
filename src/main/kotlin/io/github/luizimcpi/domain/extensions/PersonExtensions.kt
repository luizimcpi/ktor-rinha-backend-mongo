package io.github.luizimcpi.domain.extensions

import io.github.luizimcpi.domain.model.entity.Person
import io.github.luizimcpi.domain.model.request.PersonRequest
import java.util.UUID


fun PersonRequest.toPerson(): Person =
    Person(
        uuid = UUID.randomUUID().toString(),
        apelido = this.apelido,
        nome = this.nome,
        nascimento = this.nascimento,
        stack = this.stack
    )