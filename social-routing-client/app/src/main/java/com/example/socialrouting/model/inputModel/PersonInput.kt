package com.example.socialrouting.model.inputModel

data class PersonInput(
    val identifier: Int,
    val name: String,
    val email: String,
    val rating: Double,
    val routesUrl: String
)