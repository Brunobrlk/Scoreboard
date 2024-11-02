plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.googleDaggerHilt)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.bgbrlk.scoreboardbrlk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bgbrlk.scoreboardbrlk"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(findProperty("RELEASE_STORE_FILE") ?: throw GradleException("RELEASE_STORE_FILE not defined"))
            storePassword = findProperty("RELEASE_STORE_PASSWORD")?.toString() ?: throw GradleException("RELEASE_STORE_PASSWORD not defined")
            keyAlias = findProperty("RELEASE_KEY_ALIAS")?.toString() ?: throw GradleException("RELEASE_KEY_ALIAS not defined")
            keyPassword = findProperty("RELEASE_KEY_PASSWORD")?.toString() ?: throw GradleException("RELEASE_KEY_PASSWORD not defined")
        }

        val debugSigningConfig = signingConfigs.findByName("debug") ?: create("debug")

        // Set the properties for the signing configuration
        debugSigningConfig.apply {
            storeFile = file(findProperty("DEBUG_STORE_FILE") ?: throw GradleException("DEBUG_STORE_FILE not defined"))
            storePassword = findProperty("DEBUG_STORE_PASSWORD")?.toString() ?: throw GradleException("DEBUG_STORE_PASSWORD not defined")
            keyAlias = findProperty("DEBUG_KEY_ALIAS")?.toString() ?: throw GradleException("DEBUG_KEY_ALIAS not defined")
            keyPassword = findProperty("DEBUG_KEY_PASSWORD")?.toString() ?: throw GradleException("DEBUG_KEY_PASSWORD not defined")
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            ndk.debugSymbolLevel = "SYMBOL_TABLE"
        }

        debug {
            signingConfig = signingConfigs.getByName("debug")
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Ads
    implementation(libs.play.services.ads)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}