package com.envercelik.googlemapsapiworkshop.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.envercelik.googlemapsapiworkshop.R
import com.envercelik.googlemapsapiworkshop.common.Constants.ACTION_SERVICE_START
import com.envercelik.googlemapsapiworkshop.common.Constants.ACTION_SERVICE_STOP
import com.envercelik.googlemapsapiworkshop.databinding.FragmentMapsBinding
import com.envercelik.googlemapsapiworkshop.service.LocationService
import com.envercelik.googlemapsapiworkshop.ui.maps.MapsViewModel.MapState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val viewModel: MapsViewModel by viewModels()
    private var locationList = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.isMyLocationEnabled = true
        map.setOnMapLongClickListener {
            addMarker(it)
        }

        viewModel.overviewPolylinePointsOfRoute.observe(viewLifecycleOwner) {
            drawPolyline(it, Color.BLUE)
        }

        viewModel.mapViewState.observe(viewLifecycleOwner) {
            onMapViewStateChange(it)
        }

        LocationService.lastLocation.observe(this) {
            moveCamera(it)
        }

        binding.buttonStartNavigation.setOnClickListener {
            onButtonStartNavigationClick(it)
        }

        binding.buttonStopNavigation.setOnClickListener {
            onButtonStopNavigationClick(it)
        }
    }

    private fun onButtonStartNavigationClick(it: View) {
        sendActionCommandToLocationService(ACTION_SERVICE_START)
        it.visibility = View.GONE
        binding.buttonStopNavigation.visibility = View.VISIBLE
        locationList.clear()
        observeLocationList()
    }

    private fun onButtonStopNavigationClick(it: View) {
        sendActionCommandToLocationService(ACTION_SERVICE_STOP)
        it.visibility = View.GONE
        binding.buttonStartNavigation.visibility = View.VISIBLE
        locationList.clear()
    }

    private fun onMapViewStateChange(mapState: MapState) {
        when (mapState) {
            MapState.NON_DRAWABLE -> {
                map.setOnMapLongClickListener {
                    Toast.makeText(requireContext(), "please reset map", Toast.LENGTH_LONG)
                        .show()
                }
            }
            MapState.DRAWABLE -> {
                map.clear()
                map.setOnMapLongClickListener {
                    addMarker(it)
                }
                locationList.clear()
            }
        }
    }

    private fun drawPolyline(latLngList: List<LatLng> = locationList, color: Int) {
        map.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(color)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(latLngList)
            }
        )
    }

    private fun sendActionCommandToLocationService(action: String) {
        Intent(requireContext(), LocationService::class.java).apply {
            this.action = action
            requireContext().startService(this)
        }
    }

    private fun moveCamera(latLng: LatLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun addMarker(latLng: LatLng) {
        val marker = map.addMarker(MarkerOptions().position(latLng))
        marker?.let {
            viewModel.markers.add(marker)
        }
    }

    private fun observeLocationList() {
        LocationService.locationList.observe(viewLifecycleOwner, {
            if (it != null) {
                locationList = it
                drawPolyline(color = Color.YELLOW)
                followPolyline()
            }
        })
    }

    private fun followPolyline() {
        if (locationList.isNotEmpty()) {
            val lastLocation = locationList.last()
            val newCameraPosition =
                CameraUpdateFactory.newCameraPosition(setCameraPosition(lastLocation))
            map.animateCamera(newCameraPosition, 1000, null)
        }
    }

    private fun setCameraPosition(location: LatLng): CameraPosition {
        return CameraPosition.Builder()
            .target(location)
            .zoom(18f)
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}