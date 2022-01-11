package com.envercelik.googlemapsapiworkshop.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envercelik.googlemapsapiworkshop.common.Resource
import com.envercelik.googlemapsapiworkshop.data.remote.model.DirectionResponse
import com.envercelik.googlemapsapiworkshop.domain.usecase.GetDirectionsUseCase
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val getDirectionsUseCase: GetDirectionsUseCase
) : ViewModel() {

    val isButtonResetMapVisible = MutableLiveData<Boolean>(false)
    val isButtonShowRoutesVisible = MutableLiveData<Boolean>(true)

    val isButtonStopNavigationVisible = MutableLiveData(false)
    val isButtonStartNavigationVisible = MutableLiveData(true)

    private val _overviewPolylineLocationList = MutableLiveData<List<LatLng>>()
    val overviewPolylinePointsOfRoute: LiveData<List<LatLng>> = _overviewPolylineLocationList

    val markers = mutableListOf<Marker>()

    private val _mapViewState = MutableLiveData<MapState>()
    val mapViewState: LiveData<MapState> = _mapViewState

    private fun getDirection(origin: String, destination: String, key: String) {
        viewModelScope.launch {
            getDirectionsUseCase(origin, destination, key).collect {
                when (it) {
                    is Resource.Success -> onGetDirectionResponseSuccess(it.data!!)
                    is Resource.Error -> println(it.message)
                }
            }
        }
    }

    private fun onGetDirectionResponseSuccess(data: DirectionResponse) {
        val overviewPolylinePointsOfRoute = data.routes[0].overviewPolyline.points
        val listOfLatLng = PolyUtil.decode(overviewPolylinePointsOfRoute)
        _overviewPolylineLocationList.postValue(listOfLatLng)
    }

    fun onButtonShowRoutesClick() {
        if (markers.size > 1) {
            isButtonShowRoutesVisible.value = false
            isButtonResetMapVisible.value = true
            _mapViewState.value = MapState.NON_DRAWABLE

            val listOfRoutes = markers.toRouteList()

            for (route in listOfRoutes) {
                val startLocation = route.first
                val endLocation = route.second
                getDirection(startLocation, endLocation, "your_api_key")
            }
        }
    }

    private fun MutableList<Marker>.toRouteList(): List<Pair<String, String>> {
        val positionList = mutableListOf<String>()
        for (marker in this) {
            positionList.add(marker.position.toStringLatLng())
        }
        return positionList.zipWithNext()
    }

    private fun LatLng.toStringLatLng(): String {
        return "$latitude,$longitude"
    }

    fun onButtonResetMapClick() {
        isButtonResetMapVisible.value = false
        isButtonShowRoutesVisible.value = true
        _mapViewState.value = MapState.DRAWABLE
        markers.clear()
    }

    enum class MapState {
        DRAWABLE, NON_DRAWABLE
    }
}

