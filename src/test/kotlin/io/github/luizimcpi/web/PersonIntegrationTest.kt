package io.github.luizimcpi.web

import de.flapdoodle.embed.mongo.MongodProcess
import io.github.luizimcpi.configuration.configureRouting
import io.github.luizimcpi.startMongo
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
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
                "    \"stack\": [\"JAVA 8\",\"JAVA 11\",\"JAVA 17\"]\n" +
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

}