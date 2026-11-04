plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.blocker.service.monitoring"

    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Shared Core
    implementation(project(":core:common"))

    // Data Layer
    implementation(project(":core:data"))

    // Blocking Engine
    implementation(project(":engine:blocking"))

    // Android Core
    implementation("androidx.core:core-ktx:1.17.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-service:2.9.4")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.5")

    // Coroutines
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2"
    )

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
}
