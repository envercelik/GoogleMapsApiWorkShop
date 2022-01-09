package com.envercelik.googlemapsapiworkshop.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.envercelik.googlemapsapiworkshop.common.Constants.ACTION_SERVICE_START
import com.envercelik.googlemapsapiworkshop.common.Constants.ACTION_SERVICE_STOP
import com.envercelik.googlemapsapiworkshop.common.Constants.LOCATION_FASTEST_UPDATE_INTERVAL
import com.envercelik.googlemapsapiworkshop.common.Constants.LOCATION_UPDATE_INTERVAL
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class LocationService : LifecycleService() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isServiceStarted = MutableLiveData<Boolean>()
        val locationList = MutableLiveData<MutableList<LatLng>>()
        val lastLocation = MutableLiveData<LatLng>()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {
                    isServiceStarted.postValue(true)
                    startLocationUpdates()
                    getLastLocation()
                }
                ACTION_SERVICE_STOP -> {

                }
                else -> {
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        setInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setInitialValues() {
        isServiceStarted.postValue(false)
        locationList.postValue(mutableListOf())
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                val latLng = LatLng(it.result.latitude, it.result.longitude)
                lastLocation.postValue(latLng)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            for (location in result.locations) {
                val newLetLng = LatLng(location.latitude, location.longitude)
                println(newLetLng)
            }
        }
    }
}