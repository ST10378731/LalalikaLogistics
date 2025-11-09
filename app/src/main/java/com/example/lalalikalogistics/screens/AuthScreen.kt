package com.example.lalalikalogistics.screens

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.lalalikalogistics.R
import com.example.lalalikalogistics.authDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(onSuccess: () -> Unit, onAdminLogin: () -> Unit) {
    val context = LocalContext.current as Activity
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Load cached data asynchronously
    LaunchedEffect(Unit) {
        val preferences = context.authDataStore.data.first()
        email = preferences[stringPreferencesKey("email")] ?: ""
        phone = preferences[stringPreferencesKey("phone")] ?: ""
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    scope.launch {
                        context.authDataStore.edit { it[stringPreferencesKey("email")] = account.email ?: "" }
                        FirebaseFirestore.getInstance().collection("users").document(it.result.user?.uid ?: "").get()
                    }
                    onSuccess()
                }
            }
        } catch (e: ApiException) {}
    }

    var verificationId by remember { mutableStateOf<String?>(null) }
    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { /* Navigate to dashboard if logged in */ }) {
            Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.size(50.dp))
        }
        Text("Login/Register", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        if (verificationId == null) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "Toggle password visibility")
                    }
                },
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                shape = MaterialTheme.shapes.medium,
                colors = TextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = {
                isLoading = true
                scope.launch {
                    context.authDataStore.edit {
                        it[stringPreferencesKey("email")] = email
                        it[stringPreferencesKey("phone")] = phone
                    }
                }
                if (isRegister) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                errorMessage = ""
                                onSuccess()
                            } else {
                                errorMessage = task.exception?.message ?: "Registration failed"
                            }
                        }
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                errorMessage = ""
                                onSuccess()
                            } else {
                                errorMessage = task.exception?.message ?: "Sign-in failed"
                            }
                        }
                }
            }, shape = MaterialTheme.shapes.medium, enabled = !isLoading) { Text(if (isRegister) "Register" else "Sign In") }
            TextButton({ isRegister = !isRegister }) { Text(if (isRegister) "Sign In" else "Register") }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onAdminLogin, shape = MaterialTheme.shapes.medium) { Text("Admin Login") }  // Now always visible

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("1043149018553-o49etapnd48q5iitoi4g5i97u8s1ngku.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }, shape = MaterialTheme.shapes.medium) { Text("Sign In with Google") }
        } else {
            OutlinedTextField(code, { code = it }, label = { Text("Verification Code") }, shape = MaterialTheme.shapes.medium)
            Button(onClick = {
                val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { if (it.isSuccessful) onSuccess() }
            }, shape = MaterialTheme.shapes.medium) { Text("Verify Code") }
        }
    }
}
