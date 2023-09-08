package io.github.luizimcpi.domain.service

import io.github.luizimcpi.domain.model.entity.Person
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.regex

class PersonService(mongoUrl: String) {

    private val client = KMongo.createClient(mongoUrl)
    private val database = client.getDatabase("person")
    private val personCollection = database.getCollection<Person>()

    fun create(person: Person): String? {
        if(findByNickName(person.apelido).isNotEmpty()){
            throw RequestValidationException("", listOf("Impossible store person with this nickname"))
        }
        personCollection.insertOne(person)
        return person.uuid
    }

    fun findByNickName(nickname: String): List<Person> {
        return personCollection.find(
            (Person::apelido).regex(nickname, "i")
        ).toList()
    }
}