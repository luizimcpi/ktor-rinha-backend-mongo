package io.github.luizimcpi

import io.github.luizimcpi.configuration.configureRouting
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }
        application {
            val mongoUrl = environment.config.propertyOrNull("ktor.mongo.url")?.getString() ?: "mongodb://database:27017"
            configureRouting(mongoUrl)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Service Status UP", bodyAsText())
        }
    }
}
