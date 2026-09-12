package ru.rigertor.smarttravelassistant

import android.app.Application
import ru.rigertor.smarttravelassistant.di.ApplicationComponent
import ru.rigertor.smarttravelassistant.di.DaggerApplicationComponent

class TravelAssistantApp : Application() {


    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}