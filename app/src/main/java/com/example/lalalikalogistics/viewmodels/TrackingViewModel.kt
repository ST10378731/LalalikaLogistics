package com.example.lalalikalogistics.viewmodels

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrackingViewModel : ViewModel() {
    val parcelLocation = mutableStateOf<Pair<Double, Double>?>(null)
    val distance = mutableStateOf<String>("Calculating...")

    fun startTracking(shipmentId: String, homeLocation: Pair<Double, Double>) {
        val ref = FirebaseDatabase.getInstance().getReference("shipments/$shipmentId/location")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("lat").getValue(Double::class.java) ?: 0.0
                val lng = snapshot.child("lng").getValue(Double::class.java) ?: 0.0
                parcelLocation.value = Pair(lat, lng)
                // Calculate distance
                val results = FloatArray(1)
                Location.distanceBetween(homeLocation.first, homeLocation.second, lat, lng, results)
                distance.value = String.format("%.2f km", results[0] / 1000)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}