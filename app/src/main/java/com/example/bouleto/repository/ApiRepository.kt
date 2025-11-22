package com.example.bouleto.repository

import android.R.attr.level
import android.util.Log
import com.example.bouleto.models.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import java.net.URLEncoder

class ApiRepository {
    val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json()
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Ktor-Logger", message)
                }
            }
        }

    }

    suspend fun rechercheAdresse(adress: String) : ApiResponse {
        try {
            val encoded = URLEncoder.encode(adress, "UTF-8").replace("+", "%20")
            val url = "https://data.geopf.fr/geocodage/completion/?text=${encoded}&type=StreetAddress&maximumResponses=5"
            val response = client.request(url)
            return client.request(url).body()
        } catch (e: Exception) {
            Log.e("ApiRepository", "Error fetching address: ${e.message}")
            return ApiResponse()
        }
    }

}
