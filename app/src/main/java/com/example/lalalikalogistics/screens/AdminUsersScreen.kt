package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.models.User
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminUsersScreen() {
    val users = remember { mutableStateOf<List<User>>(emptyList()) }
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("users").get()
            .addOnSuccessListener { users.value = it.toObjects(User::class.java) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Manage Users", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(users.value) { user ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("UID: ${user.uid}", style = MaterialTheme.typography.bodyLarge)
                        Text("Role: ${user.role}", style = MaterialTheme.typography.bodyMedium)
                        Row {
                            IconButton(onClick = { /* Edit logic - e.g., open dialog */ }) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                            IconButton(onClick = {
                                FirebaseFirestore.getInstance().collection("users").document(user.uid).delete()
                                users.value = users.value.filter { it.uid != user.uid }
                            }) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
                        }
                    }
                }
            }
        }
    }
}