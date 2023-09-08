package io.github.luizimcpi.domain.repository

import io.github.luizimcpi.domain.model.entity.Person

interface PersonRepository {

    fun save(person: Person)

    fun findByNickname(nickName: String): List<Person>
}