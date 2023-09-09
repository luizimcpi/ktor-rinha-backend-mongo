package io.github.luizimcpi.resource.repository.mongo

import io.github.luizimcpi.domain.extensions.toPerson
import io.github.luizimcpi.domain.extensions.toPersonCollection
import io.github.luizimcpi.domain.model.entity.Person
import io.github.luizimcpi.domain.repository.PersonRepository
import io.github.luizimcpi.resource.repository.mongo.collection.MongoPersonCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.contains
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.or
import org.litote.kmongo.regex


class MongoPersonRepository: PersonRepository {

    private val mongoUrl = System.getenv("MONGO_URL") ?: "mongodb://localhost"
    private val client = KMongo.createClient(mongoUrl)
    private val database = client.getDatabase("rinha-backend-2023")
    private val personCollection = database.getCollection<MongoPersonCollection>("person")

    override fun save(person: Person): Person {
        val personMongo = person.toPersonCollection()
        personCollection.insertOne(personMongo)
        return personMongo.toPerson()
    }

    override fun findByNickname(nickname: String): List<Person> {
        return personCollection.find(
            (Person::apelido).regex(nickname, "i")
        ).map(MongoPersonCollection::toPerson).toList()
    }

    override fun findById(id: String): Person? {
        return personCollection.findOne(Person::uuid eq id)?.toPerson()
    }

    override fun findByTerm(searchTerm: String): List<Person> {
        val searchTermCapitalized = searchTerm.replaceFirstChar { it.uppercase() }
        return personCollection.find(
            or(Person::nome regex searchTerm,
                Person::apelido regex searchTerm,
                Person::stack.contains(searchTerm),
                Person::stack.contains(searchTermCapitalized)
            )).limit(MAXIMUM_PERSON_REGISTERS)
            .map(MongoPersonCollection::toPerson)
            .toList()
    }

    override fun count(): Long {
        return personCollection.countDocuments()
    }

    companion object {
        const val MAXIMUM_PERSON_REGISTERS = 50
    }
}