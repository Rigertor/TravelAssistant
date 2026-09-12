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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.rigertor.smarttravelassistant.R
import ru.rigertor.smarttravelassistant.presentation.ui.theme.Blue10
import ru.rigertor.smarttravelassistant.presentation.ui.theme.LightBlue90
import ru.rigertor.smarttravelassistant.presentation.ui.theme.LocalBackgroundGradient
import ru.rigertor.smarttravelassistant.presentation.ui.theme.White

@Composable
fun StartContent(component: StartComponent, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = LocalBackgroundGradient.current)
            .padding(16.dp)
    ) {

        // Верхние кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CircleIcon(Icons.Default.History, onClick = component::onHistoryClick)
//            CircleIcon(Icons.Default.DarkMode, onClick = component::onStartClick)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Центральная иконка
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.plane),
                    contentDescription = null,
                    tint = LightBlue90,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Заголовок
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 20.sp)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.trip_ai),
                    color = LightBlue90,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_description),
                color = LightBlue90,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Карточки
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FeatureCard(
                    stringResource(R.string.smart_routes),
                    painterResource(R.drawable.map_pin)
                )
                FeatureCard(
                    stringResource(R.string.ai_powered),
                    painterResource(R.drawable.sparkles)
                )
                FeatureCard(
                    stringResource(R.string.personalized),
                    painterResource(R.drawable.plane)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка
            Button(
                onClick = {
                    component.onStartClick()
                },
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Blue10
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Plan Your Trip", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun CircleIcon(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                clip = false
            )
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiary),
        onClick = onClick
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = LightBlue90)
    }
}

@Composable
fun FeatureCard(title: String, icon: Painter) {
    Column(
        modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = LightBlue90)
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = LightBlue90, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}