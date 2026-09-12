package ru.rigertor.smarttravelassistant.presentation.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Blue20
import ru.rigertor.smarttravelassistant.presentation.ui.theme.LocalBackgroundGradient
import ru.rigertor.smarttravelassistant.presentation.ui.theme.White

@Composable
fun StartContent(component: StartComponent, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = LocalBackgroundGradient.current)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 56.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plane),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(96.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.sparkles),
                        contentDescription = null,
                        tint = Color(0xFFFDE047),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.trip_ai),
                        color = White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.app_description),
                    color = White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    lineHeight = 28.sp
                )

                Spacer(Modifier.height(64.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureCard(
                        stringResource(R.string.smart_routes),
                        painterResource(R.drawable.map_pin),
                        Modifier.weight(1f)
                    )
                    FeatureCard(
                        stringResource(R.string.ai_powered),
                        painterResource(R.drawable.sparkles),
                        Modifier.weight(1f)
                    )
                    FeatureCard(
                        stringResource(R.string.personalized),
                        painterResource(R.drawable.plane),
                        Modifier.weight(1f)
                    )
                }
            }

            Button(
                onClick = component::onStartClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Blue20
                ),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.plan_your_trip), fontSize = 16.sp)
            }
        }
        Box(Modifier.padding(16.dp)) {
            CircleIcon(Icons.Default.History, onClick = component::onHistoryClick)
        }
    }
}

@Composable
fun CircleIcon(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.1f)),
        onClick = onClick
    ) {
        Icon(imageVector = icon, contentDescription = stringResource(R.string.trip_history), tint = White)
    }
}

@Composable
fun FeatureCard(title: String, icon: Painter, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(White.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = White.copy(alpha = 0.8f), fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}