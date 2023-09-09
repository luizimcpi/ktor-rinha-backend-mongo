package io.github.luizimcpi.web

import de.flapdoodle.embed.mongo.MongodProcess
import io.github.luizimcpi.configuration.configureRouting
import io.github.luizimcpi.startMongo
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class PersonIntegrationTest {

    private lateinit var mongodProcess: MongodProcess

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
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"Java\",\"Oracle\",\"Kotlin\"]\n" +
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
            configureRouting()
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
            configureRouting()
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
            configureRouting()
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
        assertEquals("{    \"message\": \"Campo 'apelido' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsEmptyNickname() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'apelido' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsNicknameUpperToThirtyTwoChars() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
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
        assertEquals("{    \"message\": \"Campo 'apelido' deve ter no máximo 32 caracteres\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestDoesNotContainsName() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nome' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsEmptyName() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nome' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsNameUpperToOneHundredChars() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"zpZGRQXcAElbYJQcIutRfuZzjEBqAlzu zpZGRQXcAElbYJQcIutRfuZzjEBqAlzu zpZGRQXcAElbYJQcIutRfuZzjEBqAlzu zpZGRQXcAElbYJQcIutRfuZzjEBqAlzu\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nome' deve ter no máximo 100 caracteres\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestDoesNotContainsBirthDate() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nascimento' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsEmptyBirthDate() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nascimento' é obrigatório\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsInvalidBirthDate() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"14/01/2019\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals("{    \"message\": \"Campo 'nascimento' deve seguir o formato 'AAAA-MM-DD'\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsAnIntegerValueInNicknameField() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": 1,\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"2023-09-08\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsADoubleValueInNicknameField() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": 1.2,\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"2023-09-08\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsAnIntegerValueInNameField() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": 1,\n" +
                "    \"nascimento\": \"2023-09-08\",\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsAnIntegerValueInBirthDateField() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": 1,\n" +
                "    \"stack\": [\"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun shouldCreatePersonFailWhenRequestContainsAnIntegerValueInStackField() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val request = "{\n" +
                "    \"apelido\": \"luizimcpi\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [1, \"JAVA\"]\n" +
                "}"
        val response = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun shouldFindPersonByIdPersonFailWhenDoesNotExists() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }

        val response = client.get("/pessoas/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
        }
        assertEquals("{    \"message\": \"Pessoa não encontrada\"}", response.bodyAsText().replace("\n", ""))
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun shouldFindPersonByIdPersonSuccessWhenExists() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val creationRequest = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"Java\",\"Oracle\",\"Kotlin\"]\n" +
                "}"
        val creationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(creationRequest)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)
        assertNotNull(creationResponse.headers["Location"])

        val personId = creationResponse.headers["Location"]

        val searchResponse = client.get("/pessoas/$personId") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, searchResponse.status)
        assertNotNull(searchResponse.bodyAsText())
    }

    @Test
    fun shouldFindByTermSuccessWhenNotFoundAnyRegistersInDatabase() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }

        val searchResponse = client.get("/pessoas?t=JAVA") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, searchResponse.status)
        assertNotNull(searchResponse.bodyAsText())
    }

    @Test
    fun shouldFindPersonByTermWithCompleteNicknameSuccessWhenExists() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val creationRequest = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"Java\",\"Oracle\",\"Kotlin\"]\n" +
                "}"
        val creationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(creationRequest)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)
        assertNotNull(creationResponse.headers["Location"])

        val searchResponse = client.get("/pessoas?t=luizhse") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, searchResponse.status)
        assertNotNull(searchResponse.bodyAsText())
        assertTrue(searchResponse.bodyAsText().contains("luizhse"))
    }

    @Test
    fun shouldFindPersonByTermWithPartOfNameSuccessWhenExists() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val creationRequest = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"Java\",\"Oracle\",\"Kotlin\"]\n" +
                "}"
        val creationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(creationRequest)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)
        assertNotNull(creationResponse.headers["Location"])

        val anotherCreationRequest = "{\n" +
                "    \"apelido\": \"josé\",\n" +
                "    \"nome\": \"José Roberto\",\n" +
                "    \"nascimento\": \"2000-10-01\",\n" +
                "    \"stack\": [\"C#\",\"Node\",\"Oracle\"]\n" +
                "}"
        val anotherCreationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(anotherCreationRequest)
        }
        assertEquals(HttpStatusCode.Created, anotherCreationResponse.status)
        assertNotNull(anotherCreationResponse.headers["Location"])

        val searchResponse = client.get("/pessoas?t=berto") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, searchResponse.status)
        assertNotNull(searchResponse.bodyAsText())
        assertFalse(searchResponse.bodyAsText().contains("luizhse"))
        assertTrue(searchResponse.bodyAsText().contains("Roberto"))
    }

    @Test
    fun shouldFindPersonByTermWithStackSuccessWhenExists() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            configureRouting()
        }
        val creationRequest = "{\n" +
                "    \"apelido\": \"luizhse\",\n" +
                "    \"nome\": \"Luiz\",\n" +
                "    \"nascimento\": \"1990-03-03\",\n" +
                "    \"stack\": [\"Java\",\"Oracle\",\"Kotlin\"]\n" +
                "}"
        val creationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(creationRequest)
        }
        assertEquals(HttpStatusCode.Created, creationResponse.status)
        assertNotNull(creationResponse.headers["Location"])

        val anotherCreationRequest = "{\n" +
                "    \"apelido\": \"josé\",\n" +
                "    \"nome\": \"José Roberto\",\n" +
                "    \"nascimento\": \"2000-10-01\",\n" +
                "    \"stack\": [\"C#\",\"Node\",\"Oracle\"]\n" +
                "}"
        val anotherCreationResponse = client.post("/pessoas") {
            contentType(ContentType.Application.Json)
            setBody(anotherCreationRequest)
        }
        assertEquals(HttpStatusCode.Created, anotherCreationResponse.status)
        assertNotNull(anotherCreationResponse.headers["Location"])

        val searchResponse = client.get("/pessoas?t=node") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, searchResponse.status)
        assertNotNull(searchResponse.bodyAsText())
        assertFalse(searchResponse.bodyAsText().contains("luizhse"))
        assertTrue(searchResponse.bodyAsText().contains("Roberto"))
    }

}