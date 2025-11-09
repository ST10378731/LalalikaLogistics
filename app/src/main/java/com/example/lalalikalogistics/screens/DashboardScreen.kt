package com.example.lalalikalogistics.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lalalikalogistics.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DashboardScreen(navController: NavController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var role by remember { mutableStateOf("user") }
    var activeShipments by remember { mutableStateOf(0) }
    var pendingShipments by remember { mutableStateOf(0) }

    LaunchedEffect(userId) {
        try {
            // Load user role from Firestore
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener { role = it.getString("role") ?: "user" }
            // Load real shipment counts
            FirebaseFirestore.getInstance().collection("shipments").whereEqualTo("status", "In Transit").get()
                .addOnSuccessListener { activeShipments = it.size() }
            FirebaseFirestore.getInstance().collection("shipments").whereEqualTo("status", "Pending").get()
                .addOnSuccessListener { pendingShipments = it.size() }
        } catch (e: Exception) {
            // Handle errors, e.g., log or show toast
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { navController.navigate("dashboard") }) {
            Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.size(50.dp))
        }
        Text("Dashboard", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome to Lalalika Logistics by Invincibility IT Solutions. We provide top-notch shipping services with real-time tracking and eco-friendly options. Founded by experts in logistics and technology, we're committed to innovation and customer satisfaction.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Active Shipments: $activeShipments | Pending: $pendingShipments", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))

        if (role == "admin") {
            ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, contentDescription = "Admin", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Manage Shipments", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { navController.navigate("admin_crud") }, shape = MaterialTheme.shapes.medium) { Text("Go") }
                }
            }
        } else {
            ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "Track", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Track Parcel", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { navController.navigate("track") }, shape = MaterialTheme.shapes.medium) { Text("Go") }
                }
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalShipping, contentDescription = "Courier", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Request Courier Service", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { navController.navigate("request_courier_page") }, shape = MaterialTheme.shapes.medium) { Text("Go") }
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = "About", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("About us and Create new shipping", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { navController.navigate("news") }, shape = MaterialTheme.shapes.medium) { Text("Go") }
            }
        }

        ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = "Contact", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Contact Us", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { navController.navigate("contact") }, shape = MaterialTheme.shapes.medium) { Text("Go") }
            }
        }
    }
}