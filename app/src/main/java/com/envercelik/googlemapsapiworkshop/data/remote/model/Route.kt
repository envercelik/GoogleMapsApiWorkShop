package com.envercelik.googlemapsapiworkshop.data.remote.model


import com.google.gson.annotations.SerializedName

data class Route(
    val legs: List<Leg>,
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolyline,
)

data class OverviewPolyline(
    val points: String
)