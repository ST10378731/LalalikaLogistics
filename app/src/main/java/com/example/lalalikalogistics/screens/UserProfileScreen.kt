package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserProfileScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    // Load user data from Firestore
    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    name = doc.getString("name") ?: ""
                    phone = doc.getString("phone") ?: ""
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("User Profile", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Email: ${user?.email ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
        Text("UID: ${user?.uid ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(32.dp))

        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    user?.uid?.let { uid ->
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                            .update("name", name, "phone", phone)
                    }
                    isEditing = false
                }, shape = MaterialTheme.shapes.medium) { Text("Save") }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = { isEditing = false }, shape = MaterialTheme.shapes.medium) { Text("Cancel") }
            }
        } else {
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Name: $name", style = MaterialTheme.typography.bodyLarge)
                    Text("Phone: $phone", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { isEditing = true }, shape = MaterialTheme.shapes.medium) { Text("Edit Profile") }
        }

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(onClick = { FirebaseAuth.getInstance().signOut() }, shape = MaterialTheme.shapes.medium) { Text("Logout") }
    }
}