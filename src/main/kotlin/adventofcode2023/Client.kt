package adventofcode2023

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun fetch(url: String): String {
    val client = HttpClient(CIO)
    val response = client.get(url) {
        cookie(name = "session", value = "")
    }
    return response.bodyAsText()
}