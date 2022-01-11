package com.envercelik.googlemapsapiworkshop.ui

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible", requireAll = true)
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}