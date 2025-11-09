package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lalalikalogistics.models.Shipment
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)  // Added to suppress experimental API warnings
@Composable
fun RequestCourierScreen(navController: NavController) {
    var shipmentSize by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("Small Car") }
    val vehicleOptions = listOf("Small Car", "Medium Van", "Large Truck")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Request Courier Service", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(shipmentSize, { shipmentSize = it }, label = { Text("Shipment Size (e.g., Small, Medium)") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(weight, { weight = it }, label = { Text("Weight (kg)") }, shape = MaterialTheme.shapes.medium)
        Spacer(modifier = Modifier.height(16.dp))
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = vehicleType,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                vehicleOptions.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        vehicleType = option
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val request = mapOf("size" to shipmentSize, "weight" to weight, "vehicle" to vehicleType, "status" to "Pending")
            FirebaseFirestore.getInstance().collection("courier_requests").add(request)
            navController.navigate("dashboard")
        }, shape = MaterialTheme.shapes.medium) { Text("Submit Request") }
    }
}