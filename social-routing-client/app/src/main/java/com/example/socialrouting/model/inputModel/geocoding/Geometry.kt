package com.example.socialrouting.model.inputModel.geocoding

data class Geometry (
    val location: PointGeocoding,
    val bounds: Bound,
    val location_type: String,
    val viewport: Bound
)
