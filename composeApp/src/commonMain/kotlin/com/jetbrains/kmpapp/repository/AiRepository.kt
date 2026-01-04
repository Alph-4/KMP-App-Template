package com.jetbrains.kmpapp.repository

import com.jetbrains.kmpapp.data.remote.MistralApi
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AiRepository(private val mistralApi: MistralApi) {
    private val scope = CoroutineScope(SupervisorJob())


    suspend fun getArtworkDescription(artworkName: String): String {
        return try {
            val response = mistralApi.getArtworkDescription(artworkName)
            println("DEBUG REPO data received : $response")

            // On vérifie si la réponse est null ou vide
            if (response.isNullOrBlank()) {
                "Désolé, je n'ai pas trouvé d'information sur cette œuvre."
            } else {
                response
            }
        } catch (e: RedirectResponseException) {
            // Erreurs 3xx
            "Erreur de redirection lors de la requête."
        } catch (e: ClientRequestException) {
            // Erreurs 4xx (ex: clé API invalide)
            "Erreur client : Vérifiez votre configuration API."
        } catch (e: ServerResponseException) {
            // Erreurs 5xx
            "Le service IA est temporairement indisponible."
        } catch (e: Exception) {
            // Autres erreurs (ex: pas d'internet)
            "Erreur inconnue : ${e.message}"
        }
    }
}

