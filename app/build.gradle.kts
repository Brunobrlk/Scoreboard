import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.googleDaggerHilt)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.bgbrlk.scoreboardbrlk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bgbrlk.scoreboardbrlk"
        minSdk = 26
        targetSdk = 35
        versionCode = 12
        versionName = "1.3.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        archivesName = getOutputName()
    }

    signingConfigs {
        create("release") {
            storeFile = file(findProperty("RELEASE_STORE_FILE") ?: throw GradleException("RELEASE_STORE_FILE not defined"))
            storePassword = findProperty("RELEASE_STORE_PASSWORD")?.toString() ?: throw GradleException("RELEASE_STORE_PASSWORD not defined")
            keyAlias = findProperty("RELEASE_KEY_ALIAS")?.toString() ?: throw GradleException("RELEASE_KEY_ALIAS not defined")
            keyPassword = findProperty("RELEASE_KEY_PASSWORD")?.toString() ?: throw GradleException("RELEASE_KEY_PASSWORD not defined")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            ndk.debugSymbolLevel = "FULL"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    ndkVersion = "27.0.11718014 rc1"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Ads
    implementation(libs.play.services.ads)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.config)

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

tasks.register<Zip>("zipSymbols") {
    group = "build"
    description = "Zips the merged native libs into symbols.zip."

    val outputDir = file("${project.projectDir}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib")
    val baseName = getOutputName()
    val symbolsZipName = "$baseName-symbols.zip"
    val outputZip = file("${project.projectDir}/release/$symbolsZipName")

    destinationDirectory.set(outputZip.parentFile)
    archiveFileName.set(outputZip.name)

    from(outputDir) {
        include("**/*") // Include all files and directories under 'lib'
    }
}

afterEvaluate{
    tasks.named("bundleRelease").configure {
        finalizedBy("zipSymbols")
    }
}

fun getOutputName(): String {
    val appName = "scoreboard"
    val versionName = android.defaultConfig.versionName
    val versionCode = android.defaultConfig.versionCode
    val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

    return "${appName}_${date}_v${versionName}-${versionCode}"
}