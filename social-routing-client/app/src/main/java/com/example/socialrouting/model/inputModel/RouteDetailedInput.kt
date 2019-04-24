package com.example.socialrouting.model.inputModel

import com.example.socialrouting.model.Point

data class RouteDetailedInput(
    val identifier: Long,
    val location: String,
    val name: String,
    val description: String,
    val classification: Double,
    //val categories: List<String>?,
    val duration: Int,
    val dateCreated: String,
    val points: PointCollection,
    val personIdentifier: String
)
