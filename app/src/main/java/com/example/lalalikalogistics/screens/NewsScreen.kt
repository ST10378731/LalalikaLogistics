package com.example.lalalikalogistics.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lalalikalogistics.R
import com.example.lalalikalogistics.ui.theme.PurpleGradient

@Composable
fun NewsScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Company Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("About Lalalika Logistics", style = MaterialTheme.typography.headlineLarge, color = Color.White, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        """
                        Lalalika Logistics is a cutting-edge mobile application designed to revolutionize the way users manage and track their shipments. Developed by Invincibility IT Solutions, a pioneering tech firm founded by Francina Gama and Silvia Thlalakga, Lalalika offers seamless, real-time parcel tracking with an intuitive interface that caters to both individual users and businesses. Our app integrates advanced GPS technology and Firebase-powered databases to provide accurate location updates, estimated delivery times, and comprehensive shipment histories.

                        At the heart of Lalalika is our commitment to innovation and sustainability. We prioritize eco-friendly logistics by partnering with green carriers and optimizing routes to reduce carbon footprints. Users can create shipments, request couriers based on size and weight, and even select vehicle types—from compact cars to large trucks—ensuring efficient and cost-effective deliveries. The app's admin panel allows authorized personnel to manage users, shipments, and reviews, maintaining a secure and transparent ecosystem.

                        Invincibility IT Solutions, established by visionary leaders Francina Gama and Silvia Thlalakga, specializes in developing robust, user-centric software solutions. With a focus on emerging technologies like  cloud computing, the company empowers businesses to thrive in a digital world. Francina Gama, a seasoned software engineer with expertise in mobile development, and Silvia Thlalakga, an innovative project manager with a passion for logistics, bring decades of combined experience to create tools that simplify complex processes.

                        Lalalika is more than an app; it's a copyrighted system (© 2023 Invincibility IT Solutions) that embodies our mission to bridge gaps in logistics through technology. We believe in empowering users with control, transparency, and reliability. Whether you're sending a package across town or across borders, Lalalika ensures peace of mind with 24/7 support, multi-language options, and customizable settings. Join thousands of satisfied users and experience the future of logistics today. For more information, contact us or explore our features—your next shipment is just a tap away!

                       
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.navigate("create_shipment") }, shape = MaterialTheme.shapes.medium) { Text("Get Started") }
        }
    }
}