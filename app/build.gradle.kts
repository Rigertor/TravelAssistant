plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.rigertor.smarttravelassistant"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "ru.rigertor.smarttravelassistant"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey =
            property("apiKey")?.toString() ?: error("you should add apikey into gradle.properties")
        buildConfigField(type = "String", name = "AI_API_KEY", "\"$apiKey\"")
        val id =
            property("folderId")?.toString()
                ?: error("you should add folderId into gradle.properties")
        buildConfigField(type = "String", name = "YANDEX_FOLDER_ID", "\"$id\"")
        val promptId =
            property("promptId")?.toString()
                ?: error("you should add promptId into gradle.properties")
        buildConfigField(type = "String", name = "YANDEX_PROMPT_ID", "\"$promptId\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    //Database room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    //DI Dagger2
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    //Glide (images)
    implementation(libs.glide.compose)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gsonConverter)
    implementation(libs.retrofit.logging.interceptor)
    implementation(libs.icons)

    //Google maps
    implementation(libs.maps.compose)
    implementation(libs.maps.utils)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}