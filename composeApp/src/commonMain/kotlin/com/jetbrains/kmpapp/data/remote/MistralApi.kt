package com.jetbrains.kmpapp.data.remote

import com.jetbrains.kmpapp.data.remote.model.ChatCompletionResponse
import com.jetbrains.kmpapp.data.remote.model.Message
import com.jetbrains.kmpapp.data.remote.model.MistralRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface MistralApi {
    suspend fun getArtworkDescription(artworkName: String): String?
}

class KtorMistralApi(private val client: HttpClient) : MistralApi {

    companion object {
        private const val BASE_URL =
            "https://api.mistral.ai"
    }

    override suspend fun getArtworkDescription(artworkName: String): String? {

        println("ask IA about : $artworkName")
        val prompt =
            "Agis comme un guide de musée expert et passionné. Décris l'œuvre '$artworkName' en 3 phrases maximum, est TOUJOURS en francais, ajoute egalement si possible une anecdote"

        // Création du body (pseudo-code, tu dois utiliser tes DTOs)
        val requestBody = MistralRequest(
            model = "mistral-tiny",
            messages = listOf(
                Message(role = "user", content = prompt)
            )
        )

        return try {
            val response = client.post("https://api.mistral.ai/v1/chat/completions") {
                bearerAuth("1D01EElT6NoUfeCgxjvCflboM9tOneRS") // Ou header("Authorization", "Bearer ...")
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            // Récupérer le contenu dans la réponse JSON
            val result: ChatCompletionResponse = response.body()
            println("DEBUG API data result : $result")

            result.choices.firstOrNull()?.message?.content

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}

