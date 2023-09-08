package io.github.luizimcpi.web

import io.github.luizimcpi.domain.extensions.toPerson
import io.github.luizimcpi.domain.extensions.toPersonResponse
import io.github.luizimcpi.domain.model.request.PersonRequest
import io.github.luizimcpi.domain.model.response.ErrorResponse
import io.github.luizimcpi.domain.service.PersonService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.personRouting() {

    val personService = PersonService()

    route("/pessoas") {
        post {
            val request = call.receive<PersonRequest>()
            val person = request.toPerson()

            personService.create(person)
                ?.let { userId ->
                    call.response.headers.append("Location", userId)
                    call.respond(HttpStatusCode.Created)
                } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
        }
        get("/{id}") {
            val id = call.parameters["id"].toString()
            personService.findById(id)
                ?.let { foundPerson -> call.respond(foundPerson.toPersonResponse()) }
                ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.PERSON_NOT_FOUND_RESPONSE)
        }
    }
}