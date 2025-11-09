package com.example.lalalikalogistics.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore

class ShipmentViewModel : ViewModel() {
    val shipments = mutableStateOf<List<Shipment>>(emptyList())
    val selectedShipment = mutableStateOf<Shipment?>(null)

    fun loadShipments() {
        FirebaseFirestore.getInstance().collection("shipments").get()
            .addOnSuccessListener { shipments.value = it.toObjects(Shipment::class.java) }
    }

    fun addShipment(shipment: Shipment) {
        FirebaseFirestore.getInstance().collection("shipments").add(shipment)
    }

    fun updateShipment(id: String, shipment: Shipment) {
        FirebaseFirestore.getInstance().collection("shipments").document(id).set(shipment)
    }

    fun deleteShipment(id: String) {
        FirebaseFirestore.getInstance().collection("shipments").document(id).delete()
    }

    fun selectShipment(id: String) {
        selectedShipment.value = shipments.value.find { it.id == id }
    }
}