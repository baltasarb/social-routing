package com.example.socialrouting.model.inputModel.geocoding

data class GeoCodingResponse(
    val results: List<Result>,
    val status: String
)