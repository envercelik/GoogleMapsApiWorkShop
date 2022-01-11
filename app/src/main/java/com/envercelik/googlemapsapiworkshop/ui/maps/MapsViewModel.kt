package com.envercelik.googlemapsapiworkshop.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envercelik.googlemapsapiworkshop.common.Resource
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

    fun getDirection(origin: String, destination: String, key: String) {
        viewModelScope.launch {
            val result = getDirectionsUseCase(
                origin, destination, key
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        val overviewPolylinePointsOfRoute =
                            it.data!!.routes[0].overviewPolyline.points
                        val listOfLatLng = PolyUtil.decode(overviewPolylinePointsOfRoute)
                        _overviewPolylineLocationList.postValue(listOfLatLng)
                    }
                    is Resource.Error -> println(it.message)
                }
            }
        }
    }
}