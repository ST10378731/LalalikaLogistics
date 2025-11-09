package com.example.lalalikalogistics.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lalalikalogistics.R
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShipmentScreen(navController: NavController) {
    var sender by remember { mutableStateOf("") }
    var receiver by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var freightType by remember { mutableStateOf("Small Car") }
    var trackingId by remember { mutableStateOf("") }

    val freightOptions = mapOf(
        "Small Car" to Pair("R350", "Up to 500kg"),
        "Medium Van" to Pair("R1,500", "Up to 1,500kg"),
        "Large Truck" to Pair("R5,000", "Up to 5,000kg"),
        "Heavy Duty Truck" to Pair("R15,000", "Up to 10,000kg"),
        "Mega Freight" to Pair("R700,000", "10,000kg+")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        // Logo Button
        IconButton(onClick = { navController.navigate("dashboard") }) {
            Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.size(50.dp))
        }
        Text("Create New Shipment", style = MaterialTheme.typography.headlineLarge)  // Page Heading
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(sender, { sender = it }, label = { Text("Sender Name") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(receiver, { receiver = it }, label = { Text("Receiver Name") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(address, { address = it }, label = { Text("Delivery Address") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(phone, { phone = it }, label = { Text("Phone Number") }, shape = MaterialTheme.shapes.medium)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Freight Type", style = MaterialTheme.typography.bodyLarge)
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = freightType,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                freightOptions.keys.forEach { option ->
                    DropdownMenuItem(text = { Text("$option - ${freightOptions[option]?.first} (${freightOptions[option]?.second})") }, onClick = {
                        freightType = option
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Selected: $freightType - Price: ${freightOptions[freightType]?.first} (Capacity: ${freightOptions[freightType]?.second})", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val id = UUID.randomUUID().toString()
            trackingId = id
            val shipment = Shipment(id = id, sender = sender, receiver = receiver, status = "Pending", homeLat = 0.0, homeLng = 0.0)
            FirebaseFirestore.getInstance().collection("shipments").document(id).set(shipment)
            // Store additional details including freight type
            FirebaseFirestore.getInstance().collection("shipments").document(id).collection("details").document("info").set(mapOf(
                "address" to address,
                "phone" to phone,
                "freightType" to freightType,
                "price" to freightOptions[freightType]?.first
            ))
        }, shape = MaterialTheme.shapes.medium) { Text("Create Shipment") }

        if (trackingId.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Tracking ID: $trackingId", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { navController.navigate("track/$trackingId") }, shape = MaterialTheme.shapes.medium) { Text("Track Now") }
        }
    }
}