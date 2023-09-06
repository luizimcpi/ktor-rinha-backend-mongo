package io.github.luizimcpi.domain.service

import io.github.luizimcpi.domain.model.entity.Person
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

class PersonService(mongoUrl: String) {

    private val client = KMongo.createClient(mongoUrl)
    private val database = client.getDatabase("person")
    private val personCollection = database.getCollection<Person>()

    fun create(person: Person): String? {
        personCollection.insertOne(person)
        return person.uuid
    }
}