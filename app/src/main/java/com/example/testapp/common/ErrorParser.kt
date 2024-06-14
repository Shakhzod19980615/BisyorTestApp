package com.example.testapp.common
import ErrorResponse
import android.util.Log
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
                val errorString = it.string()
                Log.d("ErrorParser", "Error Body: $errorString")
                json.decodeFromString<ErrorResponse>(errorString)
            } catch (e: Exception) {
                Log.e("ErrorParser", "Error parsing response", e)
                null
            }
        }
    }
}