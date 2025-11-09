package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.viewmodels.TrackingViewModel

@Composable
fun TrackingScreen(shipmentId: String) {
    val viewModel = remember { TrackingViewModel() }
    val homeLocation = Pair(0.0, 0.0)
    var notification by remember { mutableStateOf("") }

    LaunchedEffect(shipmentId) {
        viewModel.startTracking(shipmentId, homeLocation)
        kotlinx.coroutines.delay(5000)
        notification = "Shipment status updated to 'In Transit'!"
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tracking Shipment: $shipmentId", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (notification.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp), colors = CardDefaults.cardColors(containerColor = Color.Green.copy(alpha = 0.1f))) {
                Text(notification, style = MaterialTheme.typography.bodyMedium, color = Color.Green, modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Parcel Location: ${viewModel.parcelLocation.value?.let { "${it.first}, ${it.second}" } ?: "Updating..."}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distance from Home: ${viewModel.distance.value}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Status: In Transit", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(progress = { 0.7f }, modifier = Modifier.fillMaxWidth())  // Updated to lambda
                Text("Estimated Delivery: 2 days", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Real-time updates from Firebase Realtime Database.", style = MaterialTheme.typography.bodySmall)
    }
}