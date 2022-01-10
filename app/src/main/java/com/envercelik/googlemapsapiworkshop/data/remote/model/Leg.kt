package com.envercelik.googlemapsapiworkshop.data.remote.model


import com.google.gson.annotations.SerializedName

data class Leg(
    val distance: Distance,
    val duration: Duration,
    @SerializedName("end_address")
    val endAddress: String,
    @SerializedName("end_location")
    val endLocation: EndLocation,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_location")
    val startLocation: StartLocation,
    val steps: List<Step>,
    @SerializedName("traffic_speed_entry")
    val trafficSpeedEntry: List<Any>,
    @SerializedName("via_waypoint")
    val viaWaypoint: List<Any>
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)

data class EndLocation(
    val lat: Double,
    val lng: Double
)

data class StartLocation(
    val lat: Double,
    val lng: Double
)

data class DistanceX(
    val text: String,
    val value: Int
)