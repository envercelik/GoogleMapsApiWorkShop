package com.envercelik.googlemapsapiworkshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.envercelik.googlemapsapiworkshop.common.Permissions.hasFineLocationPermission
import com.envercelik.googlemapsapiworkshop.common.Permissions.requestFineLocationPermission
import com.envercelik.googlemapsapiworkshop.databinding.FragmentPermissionBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)

        binding.continueButton.setOnClickListener {
            if (hasFineLocationPermission(requireContext())) {
                navigateToMapScreen()
            } else {
                requestFineLocationPermission(this)
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        navigateToMapScreen()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestFineLocationPermission(this)
        }
    }

    private fun navigateToMapScreen() {
        val direction = PermissionFragmentDirections.actionPermissionFragmentToMapsFragment()
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}