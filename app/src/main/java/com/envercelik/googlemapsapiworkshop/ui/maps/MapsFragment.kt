package com.envercelik.googlemapsapiworkshop.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.envercelik.googlemapsapiworkshop.R
import com.envercelik.googlemapsapiworkshop.common.Constants.ACTION_SERVICE_START
import com.envercelik.googlemapsapiworkshop.databinding.FragmentMapsBinding
import com.envercelik.googlemapsapiworkshop.service.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private val viewModel: MapsViewModel by viewModels()

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

        viewModel.getDirection("Disneyland", "Universal+Studios+Hollywood", "your_api_key")

        viewModel.overviewPolylinePointsOfRoute.observe(viewLifecycleOwner) {
            drawPolyline(it)
        }

        LocationService.lastLocation.observe(this) {
            moveCamera(it)
        }

        sendActionCommandToLocationService(ACTION_SERVICE_START)
    }

    private fun drawPolyline(locationList: List<LatLng>) {
        map.addPolyline(
            PolylineOptions().apply {
                width(10f)
                color(Color.BLUE)
                jointType(JointType.ROUND)
                startCap(ButtCap())
                endCap(ButtCap())
                addAll(locationList)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}