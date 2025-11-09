package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ShipmentHistoryScreen() {
    val history = remember { mutableStateOf<List<Shipment>>(emptyList()) }
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("shipment_history").get()
            .addOnSuccessListener { history.value = it.toObjects(Shipment::class.java) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Shipment History", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        if (history.value.isEmpty()) {
            Text("No shipment history available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(history.value) { shipment ->
                    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${shipment.id}", style = MaterialTheme.typography.bodyLarge)
                            Text("Sender: ${shipment.sender}", style = MaterialTheme.typography.bodyMedium)
                            Text("Receiver: ${shipment.receiver}", style = MaterialTheme.typography.bodyMedium)
                            Text("Status: ${shipment.status}", style = MaterialTheme.typography.bodyMedium)
                            // Removed timestamp reference (add to Shipment model if needed)
                        }
                    }
                }
            }
        }
    }
}