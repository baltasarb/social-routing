package com.example.socialrouting.model.outputModel

import com.example.socialrouting.model.Point

data class RouteOutput (
    val location : String,
    val name : String,
    val description : String,
    val personIdentifier: Int,
    val points : List<Point>, // json in the db
    val categories : List<CategoryOutput>
)