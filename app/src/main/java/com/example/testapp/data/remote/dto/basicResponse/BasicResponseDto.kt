package com.example.testapp.data.remote.dto.basicResponse

import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

data class BasicResponseDto(
    val code: Int,
    val message: String,
    val name: String,
    val status: Int
)
fun BasicResponseDto.toBasicResponse() = BasicResponseModel(
    code = code,
    message = message,
    name = name,
    status = status
)