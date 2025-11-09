package com.example.lalalikalogistics.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color  // Added import for Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.R

@Composable
fun ContactScreen() {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Contact Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Contact Us", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (!submitted) {
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium)
                    OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Message") }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = MaterialTheme.shapes.medium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@lalalikalogistics.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Contact from $name")
                            putExtra(Intent.EXTRA_TEXT, message)
                        }
                        context.startActivity(Intent.createChooser(intent, "Send Email"))
                        submitted = true
                    }, modifier = Modifier.align(Alignment.End), shape = MaterialTheme.shapes.medium) { Text("Send Message") }
                }
            }
        } else {
            Text("Thank you! Your message has been sent.", style = MaterialTheme.typography.bodyLarge, color = Color.Green)
            Button(onClick = { submitted = false; name = ""; email = ""; message = "" }, shape = MaterialTheme.shapes.medium) { Text("Send Another") }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Phone: +1-234-567-8900\nEmail: support@lalalikalogistics.com", style = MaterialTheme.typography.bodyMedium)
    }
}