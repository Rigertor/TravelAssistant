package ru.rigertor.smarttravelassistant.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import ru.rigertor.smarttravelassistant.TravelAssistantApp
import ru.rigertor.smarttravelassistant.presentation.root.DefaultRootComponent
import ru.rigertor.smarttravelassistant.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {


    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as TravelAssistantApp).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        val rootComponent =
            rootComponentFactory.create(componentContext = defaultComponentContext())
        setContent {
            RootContent(component = rootComponent, modifier = Modifier)
        }
    }
}