package io.github.luizimcpi.domain.service

import io.github.luizimcpi.domain.model.entity.Person
import io.github.luizimcpi.domain.repository.PersonRepository
import io.ktor.server.plugins.requestvalidation.RequestValidationException

class PersonService(private val personRepository: PersonRepository) {

    fun create(person: Person): String? {
        if(personRepository.findByNickname(person.apelido).isNotEmpty()){
            throw RequestValidationException("", listOf("Impossible store person with this nickname"))
        }
        val persistedPerson = personRepository.save(person)
        return persistedPerson.uuid
    }

    fun findById(id: String): Person? {
        return personRepository.findById(id)
    }
}