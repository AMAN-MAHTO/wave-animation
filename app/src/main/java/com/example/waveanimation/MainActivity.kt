package com.example.waveanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import com.example.waveanimation.ui.theme.WaveAnimationTheme
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaveAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    waveAnimation(
                        modifier = Modifier.padding(innerPadding),
                        targetFrequency = 0.015f,
                        targetAmplitude = 0.06f,
                        targetLength = 500,
                        delayMillis = 0,
                        durationMillis = 1200,
                        endingOffset = 0,
                        initialColor = Color(0xFFFF8A80), // Light neon pink
                        targetColor = Color(0xFFFF5252),  // Darker neon pink
                        xOffset = 8f
                    )
                    waveAnimation(
                        modifier = Modifier.padding(innerPadding),
                        targetFrequency = 0.02f,
                        targetAmplitude = 0.05f,
                        targetLength = 500,
                        delayMillis = 0,
                        durationMillis = 1200,
                        endingOffset = 150,
                        initialColor = Color(0xFF80D8FF), // Light neon blue
                        targetColor = Color(0xFF40C4FF),  // Darker neon blue
                        xOffset = 7f
                    )
                    waveAnimation(
                        modifier = Modifier.padding(innerPadding),
                        targetFrequency = 0.025f,
                        targetAmplitude = 0.04f,
                        targetLength = 500,
                        delayMillis = 0,
                        durationMillis = 1200,
                        endingOffset = 100,
                        initialColor = Color(0xFFFF80AB), // Light neon magenta
                        targetColor = Color(0xFFFF4081),  // Darker neon magenta
                        xOffset = 8f
                    )


                }

                }
            }
        }
    }



@Composable
fun waveAnimation(
    modifier: Modifier = Modifier,
    targetFrequency: Float,
    targetAmplitude: Float,
    targetLength: Int,
    delayMillis: Int,
    durationMillis: Int,
    endingOffset: Int,
    targetColor: Color,
    initialColor: Color,
    xOffset: Float,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )


    val frequency by infiniteTransition.animateFloat(
        initialValue = 0.01f,
        targetValue = targetFrequency,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis / 2,

                delayMillis = delayMillis,

                ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val length by infiniteTransition.animateFloat(
        initialValue = 0.01f,
        targetValue = targetLength.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis / 2,

                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val amplitude by infiniteTransition.animateFloat(
        initialValue = 0.01f,
        targetValue = targetAmplitude,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis / 2,

                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val animatedColor by infiniteTransition.animateColor(
        initialValue = initialColor,
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis,

                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val path = Path()


        for (y in 0..length.toInt()) {
            val x = if (y < length / 2) {
                (y * amplitude) * sin((y * frequency).toDouble()).toFloat()
            } else {
                ((length - y) * amplitude) * sin((y * frequency).toDouble()).toFloat()
            }
            path.lineTo(x, y.toFloat())
        }

//        val height = ((size.height + length) * translateY) - length
        val path1 = Path()
        path1.addPath(path, offset = Offset(xOffset, 0f))
        val height = ((size.height - endingOffset) * translateY) - length
        translate(
            top = height
        ) {
            drawPath(
                path = path1,
                style = Stroke(width = 3f, cap = StrokeCap.Round),
                color = animatedColor,
                blendMode = BlendMode.SrcIn
            )
        }


        val path2 = Path()
        path2.addPath(path, Offset(size.width - xOffset, 0f))
        val height2 = ((size.height) * (1 - translateY)) + endingOffset
        translate(
            top = height2
        ) {
            drawPath(
                path = path2,
                style = Stroke(width = 3f, cap = StrokeCap.Round),
                color = animatedColor,
                blendMode = BlendMode.SrcIn
            )
        }
    }
}

