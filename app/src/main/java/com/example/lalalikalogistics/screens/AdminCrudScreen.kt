package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCrudScreen() {  // Removed navController parameter
    var shipments by remember { mutableStateOf<List<Shipment>>(emptyList()) }
    var newShipment by remember { mutableStateOf(Shipment("", "", "", "", 0.0, 0.0)) }
    var editingShipment by remember { mutableStateOf<Shipment?>(null) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("shipments").get()
            .addOnSuccessListener { shipments = it.documents.map { doc ->
                Shipment(
                    id = doc.getString("id") ?: "",
                    sender = doc.getString("sender") ?: "",
                    receiver = doc.getString("receiver") ?: "",
                    status = doc.getString("status") ?: "",
                    homeLat = doc.getDouble("homeLat") ?: 0.0,
                    homeLng = doc.getDouble("homeLng") ?: 0.0
                )
            } }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Admin CRUD - Manage Shipments", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Add/Edit Form
        OutlinedTextField(newShipment.id, { newShipment = newShipment.copy(id = it) }, label = { Text("ID") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(newShipment.sender, { newShipment = newShipment.copy(sender = it) }, label = { Text("Sender") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(newShipment.receiver, { newShipment = newShipment.copy(receiver = it) }, label = { Text("Receiver") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(newShipment.status, { newShipment = newShipment.copy(status = it) }, label = { Text("Status") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(newShipment.homeLat.toString(), { newShipment = newShipment.copy(homeLat = it.toDoubleOrNull() ?: 0.0) }, label = { Text("Home Lat") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(newShipment.homeLng.toString(), { newShipment = newShipment.copy(homeLng = it.toDoubleOrNull() ?: 0.0) }, label = { Text("Home Lng") }, shape = MaterialTheme.shapes.medium)

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (editingShipment != null) {
                // Update
                FirebaseFirestore.getInstance().collection("shipments").document(editingShipment!!.id).set(newShipment)
                editingShipment = null
            } else {
                // Add
                FirebaseFirestore.getInstance().collection("shipments").document(newShipment.id).set(newShipment)
            }
            newShipment = Shipment("", "", "", "", 0.0, 0.0)  // Reset
            // Reload
            FirebaseFirestore.getInstance().collection("shipments").get()
                .addOnSuccessListener { shipments = it.documents.map { doc ->
                    Shipment(
                        id = doc.getString("id") ?: "",
                        sender = doc.getString("sender") ?: "",
                        receiver = doc.getString("receiver") ?: "",
                        status = doc.getString("status") ?: "",
                        homeLat = doc.getDouble("homeLat") ?: 0.0,
                        homeLng = doc.getDouble("homeLng") ?: 0.0
                    )
                } }
        }, shape = MaterialTheme.shapes.medium) { Text(if (editingShipment != null) "Update" else "Add") }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(shipments) { shipment ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${shipment.id}", style = MaterialTheme.typography.bodyLarge)
                        Text("Sender: ${shipment.sender}", style = MaterialTheme.typography.bodyMedium)
                        Text("Receiver: ${shipment.receiver}", style = MaterialTheme.typography.bodyMedium)
                        Text("Status: ${shipment.status}", style = MaterialTheme.typography.bodyMedium)
                        Row {
                            Button(onClick = {
                                editingShipment = shipment
                                newShipment = shipment
                            }, shape = MaterialTheme.shapes.medium) { Text("Edit") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                FirebaseFirestore.getInstance().collection("shipments").document(shipment.id).delete()
                                // Reload
                                FirebaseFirestore.getInstance().collection("shipments").get()
                                    .addOnSuccessListener { shipments = it.documents.map { doc ->
                                        Shipment(
                                            id = doc.getString("id") ?: "",
                                            sender = doc.getString("sender") ?: "",
                                            receiver = doc.getString("receiver") ?: "",
                                            status = doc.getString("status") ?: "",
                                            homeLat = doc.getDouble("homeLat") ?: 0.0,
                                            homeLng = doc.getDouble("homeLng") ?: 0.0
                                        )
                                    } }
                            }, shape = MaterialTheme.shapes.medium) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }
}
