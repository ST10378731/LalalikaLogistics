

package com.example.lalalikalogistics.models

data class Shipment(
    val id: String,
    val sender: String,
    val receiver: String,
    val status: String,
    val homeLat: Double,
    val homeLng: Double
)