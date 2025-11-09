package com.example.lalalikalogistics.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ReviewsScreen() {
    val reviews = remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var newReview by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("reviews").get()
            .addOnSuccessListener { reviews.value = it.documents.mapNotNull { doc -> doc.data?.mapKeys { it.key }?.mapValues { it.value.toString() } } }  // Fixed cast
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reviews & Feedback", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(newReview, { newReview = it }, label = { Text("Add Your Review") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
            Text("Post Anonymously", style = MaterialTheme.typography.bodyMedium)
        }
        if (!isAnonymous) {
            OutlinedTextField(userName, { userName = it }, label = { Text("Your Name") }, shape = MaterialTheme.shapes.medium)
            OutlinedTextField(userEmail, { userEmail = it }, label = { Text("Your Email") }, shape = MaterialTheme.shapes.medium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val reviewData = if (isAnonymous) {
                mapOf("review" to newReview, "anonymous" to "true")
            } else {
                mapOf("review" to newReview, "name" to userName, "email" to userEmail)
            }
            FirebaseFirestore.getInstance().collection("reviews").add(reviewData)
            newReview = ""
            userName = ""
            userEmail = ""
            // Reload reviews
            FirebaseFirestore.getInstance().collection("reviews").get()
                .addOnSuccessListener { reviews.value = it.documents.mapNotNull { doc -> doc.data?.mapKeys { it.key }?.mapValues { it.value.toString() } } }  // Fixed cast
        }, shape = MaterialTheme.shapes.medium) { Text("Submit Review") }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(reviews.value) { review ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(8.dp), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(review["review"] ?: "", style = MaterialTheme.typography.bodyMedium)
                        if (review["anonymous"] != "true") {
                            Text("By: ${review["name"] ?: "Unknown"} (${review["email"] ?: ""})", style = MaterialTheme.typography.bodySmall)
                        } else {
                            Text("Anonymous", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}