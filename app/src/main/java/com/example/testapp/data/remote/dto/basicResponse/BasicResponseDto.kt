package com.example.testapp.data.remote.dto.basicResponse

import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

data class BasicResponseDto(
    val name: String,
    val message: String,
    val code: Int,
    val status: Int
)
fun BasicResponseDto.toBasicResponseModel() = BasicResponseModel(
    code = code,
    message = message,
    name = name,
    status = status
)