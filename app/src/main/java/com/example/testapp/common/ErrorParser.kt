package com.example.testapp.common
import ErrorResponse
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.io.IOException

class ErrorParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parseError(response: Response<*>): ErrorResponse? {
        val errorBody = response.errorBody()
        return errorBody?.let {
            try {
                json.decodeFromString<ErrorResponse>(it.string())
            } catch (e: Exception) {
                null
            }
        }
    }
}