package org.example.project

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Greeting {
    private val client = HttpClient()

    suspend fun greeting(): String {
        val response = client.get("https://jsonplaceholder.typicode.com/posts")
        return response.bodyAsText()
    }

    fun parsePosts(jsonString: String): List<Post> {
        return try {
            Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

