package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ShipmentSelectionScreen(navController: NavController) {
    var shipments by remember { mutableStateOf<List<Shipment>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedShipment by remember { mutableStateOf<Shipment?>(null) }

    LaunchedEffect(Unit) {
        try {
            FirebaseFirestore.getInstance().collection("shipments").get()
                .addOnSuccessListener { querySnapshot ->
                    shipments = querySnapshot.documents.map { doc ->
                        Shipment(
                            id = doc.getString("id") ?: "",
                            sender = doc.getString("sender") ?: "",
                            receiver = doc.getString("receiver") ?: "",
                            status = doc.getString("status") ?: "",
                            homeLat = doc.getDouble("homeLat") ?: 0.0,
                            homeLng = doc.getDouble("homeLng") ?: 0.0
                        )
                    }
                }
        } catch (e: Exception) {
            // Handle errors
        }
    }

    val filteredShipments = shipments.filter { it.id.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = { navController.navigate("dashboard") }) {
            // Add logo if needed
        }
        Text("Track Your Shipment", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Enter Tracking ID") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            selectedShipment = filteredShipments.find { it.id == searchQuery }
        }, shape = MaterialTheme.shapes.medium) { Text("Search") }

        if (selectedShipment != null) {
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tracking ID: ${selectedShipment!!.id}", style = MaterialTheme.typography.bodyLarge)
                    Text("Sender: ${selectedShipment!!.sender}", style = MaterialTheme.typography.bodyMedium)
                    Text("Receiver: ${selectedShipment!!.receiver}", style = MaterialTheme.typography.bodyMedium)
                    Text("Status: ${selectedShipment!!.status}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    val location = "Lat: ${selectedShipment!!.homeLat}, Lng: ${selectedShipment!!.homeLng}"
                    val distance = "Distance: ${selectedShipment!!.homeLat * 10} km away"
                    Text("Courier Location: $location", style = MaterialTheme.typography.bodyMedium)
                    Text(distance, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { navController.navigate("track/${selectedShipment!!.id}") }, shape = MaterialTheme.shapes.medium) { Text("Track This") }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("All Shipments:", style = MaterialTheme.typography.headlineSmall)
        LazyColumn {
            items(filteredShipments) { shipment ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tracking ID: ${shipment.id}", style = MaterialTheme.typography.bodyLarge)
                        Text("Sender: ${shipment.sender}", style = MaterialTheme.typography.bodyMedium)
                        Text("Receiver: ${shipment.receiver}", style = MaterialTheme.typography.bodyMedium)
                        Text("Status: ${shipment.status}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { navController.navigate("track/${shipment.id}") }, shape = MaterialTheme.shapes.medium) { Text("Track This") }
                    }
                }
            }
        }
    }
}