package com.envercelik.googlemapsapiworkshop.data.remote.model


import com.google.gson.annotations.SerializedName

data class Step(
    val distance: DistanceX,
    val duration: DurationX,
    @SerializedName("end_location")
    val endLocation: EndLocationX,
    @SerializedName("html_instructions")
    val htmlInstructions: String,
    val maneuver: String,
    val polyline: Polyline,
    @SerializedName("start_location")
    val startLocation: StartLocationX,
    @SerializedName("travel_mode")
    val travelMode: String
)

data class DurationX(
    val text: String,
    val value: Int
)

data class EndLocationX(
    val lat: Double,
    val lng: Double
)

data class StartLocationX(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)