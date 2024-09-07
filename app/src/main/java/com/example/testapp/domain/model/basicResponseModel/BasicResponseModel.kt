package com.example.testapp.domain.model.basicResponseModel

data class BasicResponseModel (
    val code: Int=0,
    val message: String?=null,
    val name: String?=null,
    val status: Int=0
)