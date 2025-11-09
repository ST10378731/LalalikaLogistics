package com.example.lalalikalogistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lalalikalogistics.screens.*
import com.example.lalalikalogistics.ui.theme.LalalikaLogisticsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LalalikaLogisticsTheme {
                val navController = rememberNavController()
                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
                var userRole by remember { mutableStateOf("user") }

                // Fetch user role
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        FirebaseFirestore.getInstance().collection("users").document(userId).get()
                            .addOnSuccessListener { userRole = it.getString("role") ?: "user" }
                    }
                }

                if (isLoggedIn) {
                    Scaffold(bottomBar = { BottomNavigationBar(navController, userRole) }) { padding ->
                        AppNavHost(navController, Modifier.padding(padding))
                    }
                } else {
                    AppNavHost(navController, Modifier)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "dashboard" else "splash"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("splash") { SplashScreen { navController.navigate("auth") } }
        composable("auth") { AuthScreen(onSuccess = { navController.navigate("dashboard") }, onAdminLogin = { navController.navigate("admin_auth") }) }
        composable("admin_auth") { AdminAuthScreen(onSuccess = { navController.navigate("admin") }, onBack = { navController.navigate("auth") }) }  // Fixed: Navigate to admin dashboard
        composable("dashboard") { DashboardScreen(navController) }
        composable("track") { ShipmentSelectionScreen(navController) }
        composable("track/{shipmentId}") { backStackEntry ->
            val shipmentId = backStackEntry.arguments?.getString("shipmentId") ?: ""
            TrackingScreen(shipmentId)
        }
        composable("admin") { AdminDashboardScreen(navController) }
        composable("admin_crud") { AdminCrudScreen() }
        composable("admin_users") { AdminUsersScreen() }
        composable("profile") { UserProfileScreen() }
        composable("history") { ShipmentHistoryScreen() }
        composable("settings") { SettingsScreen() }
        composable("news") { NewsScreen(navController) }
        composable("contact") { ContactScreen() }
        composable("create_shipment") { CreateShipmentScreen(navController) }
        composable("reviews") { ReviewsScreen() }
        composable("request_courier") { RequestCourierScreen(navController) }
        composable("confirm") { ShipmentConfirmationScreen { navController.navigate("dashboard") } }
        composable("request_courier_page") { RequestCourierPage(navController) }
        composable("add_announcements") { AddAnnouncementsScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, userRole: String) {
    val items = if (userRole == "admin") {
        listOf("dashboard", "track", "add_announcements", "reviews", "settings")
    } else {
        listOf("dashboard", "track", "history", "reviews", "settings")
    }
    val icons = listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.Build, Icons.AutoMirrored.Filled.Message, Icons.Default.Settings)
    val labels = if (userRole == "admin") {
        listOf("Home", "Track", "Announcements", "Reviews", "Settings")
    } else {
        listOf("Home", "Track", "History", "Reviews", "Settings")
    }

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = labels[index]) },
                label = { Text(labels[index]) },
                selected = currentRoute == item,
                onClick = { navController.navigate(item) }
            )
        }
    }
}