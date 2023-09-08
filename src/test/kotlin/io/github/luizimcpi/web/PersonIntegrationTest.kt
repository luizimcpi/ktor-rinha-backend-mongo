package io.github.luizimcpi.web

import de.flapdoodle.embed.mongo.MongodProcess
import io.github.luizimcpi.configuration.configureRouting
import io.github.luizimcpi.startMongo
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class PersonIntegrationTest {

    lateinit var mongodProcess: MongodProcess

    @BeforeTest
    fun setUp(){
        mongodProcess = startMongo("localhost", 27017)
    }

    @AfterTest
    fun shutDown(){
        mongodProcess.stop()
    }

    @Test
    fun shouldCreatePersonSuccessWhenRequestIsValid() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost:27017"
            configureRouting(mongoUrl)
        }
        val request = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertNotNull(response.headers["Location"])
    }

    @Test
    fun shouldCreatePersonSuccessWhenRequestDoesNotContainsAStack() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost:27017"
            configureRouting(mongoUrl)
        }
        val request = "{\n" +
                "    \"apelido\": \"luzicmpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\"\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenAlreadyExistsSameNicknameInDatabase() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost:27017"
            configureRouting(mongoUrl)
        }
        val request = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\"\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.Created, response.status)

        val invalidRequest = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\"\n" +
                "}"
        val invalidResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(invalidRequest)
        }
        assertEquals(HttpStatusCode.UnprocessableEntity, invalidResponse.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestDoesNotContainsNickname() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost:27017"
            configureRouting(mongoUrl)
        }
        val request = "{\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsNicknameUpperToThirtyTwoChars() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://localhost:27017"
            configureRouting(mongoUrl)
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpiYFghrWQEcAsxzHKLoPwTUIpfsPvx\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }


}