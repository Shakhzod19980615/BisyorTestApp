package com.example.testapp.data.repositoryImpl.favourite

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.favourite.ChangeFavoriteStatusResponse
import com.example.testapp.data.remote.dto.favourite.toChangeFavouriteModel
import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.domain.model.ChangeFavouriteModel
import com.example.testapp.domain.repository.favourite.ChangeFavouriteStatusRepository
import javax.inject.Inject

import android.util.Log

class ChangeFavouriteStatusRepImpl @Inject constructor(
    private val api: AppService
) : ChangeFavouriteStatusRepository {

    override suspend fun likeItem(param: ChangeFavoriteStatusRequest): ChangeFavouriteModel {
        // Log the input parameters
        Log.d("ChangeFavouriteStatusRepImpl", "Request: $param")

        return try {
            // Make the API call
            val response = api.likeItem(param)

            // Log the raw API response
            Log.d("ChangeFavouriteStatusRepImpl", "Raw API Response: $response")

            // Convert the response to the model
            val changeFavouriteModel = response.toChangeFavouriteModel()

            // Log the converted model
            Log.d("ChangeFavouriteStatusRepImpl", "Converted Model: $changeFavouriteModel")

            // Return the converted model
            changeFavouriteModel
        } catch (e: Exception) {
            if (e is retrofit2.HttpException && e.code() == 404) {
                // Log specific details for 404 errors
                Log.e("ChangeFavouriteStatusRepImpl", "HTTP 404: Endpoint or resource not found")
            } else {
                // Log other errors
                Log.e("ChangeFavouriteStatusRepImpl", "Error: ${e.message}", e)
            }

            // Return an error model
            return ChangeFavouriteModel("", e.message.toString(), emptyList())

        }
    }

    override suspend fun getUserFavoriteIds(): List<Int> {
        return try {
            api.getUserFavoriteIds()
        } catch (e: Exception) {
            emptyList()
        }
    }
}


