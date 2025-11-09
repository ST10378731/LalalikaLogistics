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
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestCourierPage(navController: NavController) {
    var shipmentName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var productValue by remember { mutableStateOf("") }
    var insurance by remember { mutableStateOf(0.0) }
    var messageToDriver by remember { mutableStateOf("") }

    // Calculate insurance when product value changes
    LaunchedEffect(productValue) {
        val value = productValue.toDoubleOrNull() ?: 0.0
        insurance = value * 0.15
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        // Logo Button
        IconButton(onClick = { navController.navigate("dashboard") }) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp)
            )
        }
        Text("Request Courier Truck", style = MaterialTheme.typography.headlineLarge)  // Page Heading
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(shipmentName, { shipmentName = it }, label = { Text("Shipment Name") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(description, { description = it }, label = { Text("What is it? (Description)") }, shape = MaterialTheme.shapes.medium)
        OutlinedTextField(productValue, { productValue = it }, label = { Text("Product Value (R)") }, shape = MaterialTheme.shapes.medium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Insurance (15%): R${"%.2f".format(insurance)}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(messageToDriver, { messageToDriver = it }, label = { Text("Message to Driver") }, shape = MaterialTheme.shapes.medium, maxLines = 3)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val request = mapOf(
                "shipmentName" to shipmentName,
                "description" to description,
                "productValue" to productValue,
                "insurance" to insurance,
                "messageToDriver" to messageToDriver,
                "status" to "Pending"
            )
            FirebaseFirestore.getInstance().collection("courier_requests").add(request)
            navController.navigate("dashboard")
        }, shape = MaterialTheme.shapes.medium) { Text("Submit Request") }
    }
}