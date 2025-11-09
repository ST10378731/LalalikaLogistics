package com.example.lalalikalogistics.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.lalalikalogistics.R
import com.example.lalalikalogistics.settingsDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("English") }
    var currentLanguage by remember { mutableStateOf("English") }

    // Load saved settings
    LaunchedEffect(Unit) {
        val preferences = context.settingsDataStore.data.first()
        notificationsEnabled = preferences[stringPreferencesKey("notifications")] == "true"
        darkModeEnabled = preferences[stringPreferencesKey("dark_mode")] == "true"
        currentLanguage = preferences[stringPreferencesKey("language")] ?: "English"
        language = currentLanguage
    }

    val languages = mapOf(
        "English" to "Welcome",
        "Spanish" to "Bienvenido",
        "French" to "Bienvenue",
        "Zulu" to "Sawubona",
        "Afrikaans" to "Goeiedag"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        IconButton(onClick = { /* Navigate to dashboard */ }) {
            Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.size(50.dp))
        }
        Text("Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Enable Notifications", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it })
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Language", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = language,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languages.keys.forEach { lang ->
                    DropdownMenuItem(text = { Text(lang) }, onClick = {
                        language = lang
                        currentLanguage = lang
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Selected Language Greeting: ${languages[currentLanguage] ?: "Hello"}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            scope.launch {
                context.settingsDataStore.edit {
                    it[stringPreferencesKey("notifications")] = notificationsEnabled.toString()
                    it[stringPreferencesKey("dark_mode")] = darkModeEnabled.toString()
                    it[stringPreferencesKey("language")] = currentLanguage
                }
            }
            // Apply changes (e.g., restart or update UI)
        }, shape = MaterialTheme.shapes.medium) { Text("Save Settings") }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { FirebaseAuth.getInstance().signOut() }, shape = MaterialTheme.shapes.medium) { Text("Logout") }
    }
}
