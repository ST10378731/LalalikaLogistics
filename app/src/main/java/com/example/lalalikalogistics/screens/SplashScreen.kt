package com.example.lalalikalogistics.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lalalikalogistics.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    var alpha by remember { mutableStateOf(0f) }
    val animatedAlpha by animateFloatAsState(targetValue = alpha)
    LaunchedEffect(Unit) {
        alpha = 1f
        delay(2000)
        onFinish()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFA020F0), Color.White))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp).graphicsLayer(alpha = animatedAlpha)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Lalalika Logistics",
                color = Color.White.copy(alpha = animatedAlpha),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}