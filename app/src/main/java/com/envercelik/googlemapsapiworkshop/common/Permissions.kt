package com.envercelik.googlemapsapiworkshop.common

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import androidx.fragment.app.Fragment
import com.envercelik.googlemapsapiworkshop.common.Constants.PERMISSION_FINE_LOCATION_REQUEST_CODE
import com.vmadalin.easypermissions.EasyPermissions


object Permissions {

    fun hasFineLocationPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            ACCESS_FINE_LOCATION
        )

    fun requestFineLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "This application cannot work without location permission",
            PERMISSION_FINE_LOCATION_REQUEST_CODE,
            ACCESS_FINE_LOCATION
        )
    }
}