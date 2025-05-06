plugins {
    alias(libs.plugins.android.application)
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.exp.firebaseauthexample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.exp.firebaseauthexample"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.junit.jupiter) // Use the latest version
    testImplementation(libs.junit.jupiter.api) // Ensure JUnit 5 is used
    androidTestImplementation (libs.androidx.core.testing) // Or the latest version
    androidTestImplementation (libs.androidx.junit.v115) // For AndroidX JUnit Runner
    androidTestImplementation (libs.androidx.espresso.core.v351)
}