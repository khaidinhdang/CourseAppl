plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.courseapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.courseapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.firebase.auth)

    implementation(libs.firebase.analytics)

    implementation(libs.firebase.storage)

    implementation(libs.firebase.database)

    implementation(libs.firebase.firestore)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.room.ktx)
    implementation(fileTree(mapOf(
        "dir" to "D:\\tải xuống\\zalopaylib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.1")

    implementation("androidx.compose.material:material:1.3.1")

    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha07")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha07")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")

    //coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    //video
    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation ("androidx.media3:media3-ui:1.0.0")

    //bill
    implementation ("com.android.billingclient:billing:5.1.0")
    //livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")

    implementation("com.composables.ui:menu:1.4.0")
}