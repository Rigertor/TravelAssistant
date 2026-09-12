package ru.rigertor.smarttravelassistant.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirplanemodeActive
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Blue20
import kotlin.random.Random

@Composable
fun LoadingContentCheck(
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(label = "plane_rotation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_animation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 34.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // AIRPLANE ICON
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {


                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Blue20.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Icon(
                    modifier = Modifier
                        .size(72.dp)
                        .graphicsLayer(
                            rotationZ = rotation,
                            scaleX = scale,
                            scaleY = scale
                        ),
                    painter = painterResource(R.drawable.plane),
                    contentDescription = null,
                    tint = Blue20
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.planning_your_perfect_trip),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.this_will_only_take_a_moment),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            LoadingStepCard(
                icon = Icons.Outlined.AutoAwesome,
                title = stringResource(R.string.analyzing_your_preferences),
                initialValueScale = Random.nextFloat()
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoadingStepCard(
                icon = Icons.Outlined.LocationOn,
                title = stringResource(R.string.finding_the_best_places),
                initialValueScale = Random.nextFloat()
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoadingStepCard(
                icon = Icons.Outlined.AirplanemodeActive,
                title = stringResource(R.string.planning_your_perfect_route),
                initialValueScale = Random.nextFloat()
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(100.dp)),
                color = Blue20,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun LoadingStepCard(
    icon: ImageVector,
    title: String,
    initialValueScale: Float = 1f
) {

    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = initialValueScale,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                imageVector = icon,
                contentDescription = null,
                tint = Blue20
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun LoadingContentPrev() {
    LoadingContentCheck()
}