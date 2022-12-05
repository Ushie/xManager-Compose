package dev.ushiekane.xmanager.di

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val httpModule = module {
    fun provideHttpClient() = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
                followSslRedirects(true)
            }
        }
        BrowserUserAgent()
        install(ContentNegotiation) {
            json(
                json = Json {
                encodeDefaults = true
                isLenient = true
                ignoreUnknownKeys = true
            },
            contentType = ContentType.Text.Plain
            )
        }
    }
    singleOf(::provideHttpClient)
}