package io.github.luizimcpi.resource.repository.mongo

import io.github.luizimcpi.domain.extensions.toPerson
import io.github.luizimcpi.domain.extensions.toPersonCollection
import io.github.luizimcpi.domain.model.entity.Person
import io.github.luizimcpi.domain.repository.PersonRepository
import io.github.luizimcpi.resource.repository.mongo.collection.MongoPersonCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.regex

class MongoPersonRepository: PersonRepository {

    private val mongoUrl = System.getenv("MONGO_URL") ?: "mongodb://localhost"
    private val client = KMongo.createClient(mongoUrl)
    private val database = client.getDatabase("rinha-backend-2023")
    private val personCollection = database.getCollection<MongoPersonCollection>("person")

    override fun save(person: Person) {
        val personMongo = person.toPersonCollection()
        personCollection.insertOne(personMongo)
    }

    override fun findByNickname(nickname: String): List<Person> {
        return personCollection.find(
            (Person::apelido).regex(nickname, "i")
        ).map(MongoPersonCollection::toPerson).toList()
    }
}