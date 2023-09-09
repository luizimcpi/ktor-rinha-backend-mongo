package io.github.luizimcpi.configuration

import io.github.luizimcpi.domain.repository.PersonRepository
import io.github.luizimcpi.domain.service.PersonService
import io.github.luizimcpi.resource.repository.mongo.MongoPersonRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val appModule = module {
    single<PersonRepository> { MongoPersonRepository() }
    single { PersonService(get()) }
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(appModule)
    }
}