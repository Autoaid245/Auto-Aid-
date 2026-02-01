import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.project.auto_aid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.project.auto_aid"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Android & Utilities
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // This is the line that helps fix the red underline in themes.xml
    implementation(libs.material3)
    implementation("com.google.android.material:material:1.11.0")


    // Jetpack Compose (using BOM - Bill of Materials)
    // The BOM ensures all the following Compose libraries have compatible versions.

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")


    // CORRECTED: Only one declaration of material3, letting the BOM control the version.
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Firebase (using BOM)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(platform("com.google.firebase:firebase-auth-ktx:23.2.1"))
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation ("com.google.firebase:firebase-storage-ktx:21.0.1")


    implementation("com.google.android.gms:play-services-auth:21.0.1")
    implementation("com.google.firebase:firebase-auth-ktx")

    implementation("com.google.firebase:firebase-firestore:26.0.2")
    implementation(libs.firebase.ai)
    implementation(libs.androidx.ui.text)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.camera.core)
    implementation(libs.espresso.core)
    implementation("androidx.compose.material3:material3:<latest-version>")
    implementation("androidx.compose.material:material-icons-extended:<matching-version>")
    implementation(libs.androidx.compilercommon)
    implementation(libs.firebase.storage)
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation(libs.androidx.runtime)
    implementation(libs.foundation)
    implementation(libs.androidx.animation.core)
    implementation(libs.runtime)
    implementation(libs.androidx.compose.foundation.foundation)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01")) // BOM for test dependencies too
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}