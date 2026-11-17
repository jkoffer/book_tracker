package com.jk.book_tracker.features.scanner

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScannerVisualEffect() {
    val transition = rememberInfiniteTransition(label = "scanner")

    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = CubicBezierEasing(0.4f, 0.1f, 0.6f, 0.9f),
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        val left = size.width * 0.1f
        val top = size.height * 0.35f
        val width = size.width * 0.8f
        val height = size.height * 0.2f

        val right = left + width
        val bottom = top + height

        val cornerLength = 30.dp.toPx()
        val borderWidth = 3.dp.toPx()

        val offsetY = top + height * progress

        // Backdrop
        drawRect(Color.Black.copy(alpha = 0.5f))

        // Transparent window
        drawRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(width, height),
            blendMode = BlendMode.Clear,
        )

        // Window border
        drawRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(width, height),
            style = Stroke(borderWidth)
        )

        // Left corner
        drawLine(
            Color.Transparent,
            Offset(left, top + cornerLength),
            Offset(left, bottom - cornerLength),
            borderWidth + 1.dp.toPx(),
            blendMode = BlendMode.Clear
        )

        // Right corner
        drawLine(
            Color.Transparent,
            Offset(right, top + cornerLength),
            Offset(right, bottom - cornerLength),
            borderWidth + 1.dp.toPx(),
            blendMode = BlendMode.Clear
        )

        // Top corner
        drawLine(
            Color.Transparent,
            Offset(left + cornerLength, top),
            Offset(right - cornerLength, top),
            borderWidth + 1.dp.toPx(),
            blendMode = BlendMode.Clear
        )

        // Bottom corner
        drawLine(
            Color.Transparent,
            Offset(left + cornerLength, bottom),
            Offset(right - cornerLength, bottom),
            borderWidth + 1,
            blendMode = BlendMode.Clear
        )

        // Red animated line
        drawLine(
            color = Color.Red,
            start = Offset(left, offsetY),
            end = Offset(left + width, offsetY),
            strokeWidth = 2.dp.toPx(),
        )
    }
}

@Preview
@Composable
fun ScannerVisualEffectPreview() {
    ScannerVisualEffect()
}