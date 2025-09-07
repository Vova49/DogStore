package com.example.dogstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Composable component that displays a smooth loading spinner with fade animations.
 * Ensures visibility even for very fast loading times.
 */
@Composable
fun LoadingSpinner(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    // State to ensure minimum display time for very fast loads
    var shouldShowSpinner by remember { mutableStateOf(false) }
    
    LaunchedEffect(isLoading) {
        if (isLoading) {
            // Show immediately
            shouldShowSpinner = true
        } else {
            // Add small delay before hiding for smooth transition
            delay(300)
            shouldShowSpinner = false
        }
    }
    
    AnimatedVisibility(
        visible = shouldShowSpinner,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 200,
                easing = FastOutSlowInEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Custom animated spinner with smooth rotation
                val infiniteTransition = rememberInfiniteTransition(label = "spinner_rotation")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1200,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "rotation"
                )
                
                // Modern Material Design spinner
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotation),
                    color = Color(0xFF1976D2),
                    strokeWidth = 4.dp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Loading...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

/**
 * Alternative spinner with pulsing dots for variety
 */
@Composable
fun PulsingDotsSpinner(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var shouldShowSpinner by remember { mutableStateOf(false) }
    
    LaunchedEffect(isLoading) {
        if (isLoading) {
            shouldShowSpinner = true
        } else {
            delay(300)
            shouldShowSpinner = false
        }
    }
    
    AnimatedVisibility(
        visible = shouldShowSpinner,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            // Custom pulsing dots animation
            Row {
                val infiniteTransition = rememberInfiniteTransition(label = "dots_pulse")
                
                repeat(3) { index ->
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 600,
                                delayMillis = index * 200
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_$index"
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1976D2))
                            .alignBy { 0 }
                    )
                    
                    if (index < 2) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}