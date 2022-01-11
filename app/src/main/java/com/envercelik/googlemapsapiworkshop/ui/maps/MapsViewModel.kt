package com.envercelik.googlemapsapiworkshop.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.envercelik.googlemapsapiworkshop.common.Resource
import com.envercelik.googlemapsapiworkshop.domain.usecase.GetDirectionsUseCase
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

    fun getDirection() {
        viewModelScope.launch {
            val result = getDirectionsUseCase(
                "Disneyland", "Universal+Studios+Hollywood",
                "your_api_key"
            ).collect {
                when (it) {
                    is Resource.Success -> println(it.data!!.routes)
                    is Resource.Error -> println(it.message)
                }
            }
        }
    }
}