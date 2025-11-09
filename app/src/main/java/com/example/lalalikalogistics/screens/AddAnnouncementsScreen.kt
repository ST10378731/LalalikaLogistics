package com.example.lalalikalogistics.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.R
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnnouncementsScreen() {
    var announcements by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var newAnnouncement by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            // Load real announcements from Firestore
            FirebaseFirestore.getInstance().collection("announcements").get()
                .addOnSuccessListener { announcements = it.documents.map { it.data ?: emptyMap() } }
        } catch (e: Exception) {
            // Handle errors
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { /* Navigate to dashboard */ }) {
            Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.size(50.dp))
        }
        Text("Add Announcements", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(newAnnouncement, { newAnnouncement = it }, label = { Text("New Announcement") }, shape = MaterialTheme.shapes.medium, maxLines = 3)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (newAnnouncement.isNotEmpty()) {
                FirebaseFirestore.getInstance().collection("announcements").add(mapOf("text" to newAnnouncement, "timestamp" to System.currentTimeMillis()))
                newAnnouncement = ""
                // Reload
                FirebaseFirestore.getInstance().collection("announcements").get()
                    .addOnSuccessListener { announcements = it.documents.map { it.data ?: emptyMap() } }
            }
        }, shape = MaterialTheme.shapes.medium) { Text("Add Announcement") }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(announcements) { announcement ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(announcement["text"] as? String ?: "", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            // Delete (adjust based on your document structure)
                            FirebaseFirestore.getInstance().collection("announcements").whereEqualTo("text", announcement["text"]).get()
                                .addOnSuccessListener { docs ->
                                    docs.documents.forEach { it.reference.delete() }
                                    // Reload
                                    FirebaseFirestore.getInstance().collection("announcements").get()
                                        .addOnSuccessListener { announcements = it.documents.map { it.data ?: emptyMap() } }
                                }
                        }, shape = MaterialTheme.shapes.medium) { Text("Delete") }
                    }
                }
            }
        }
    }
}