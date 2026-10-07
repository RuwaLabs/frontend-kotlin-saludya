plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    val resetLinkHost = providers.gradleProperty("saludya.resetLinkHost").getOrElse("example.invalid")
    require(resetLinkHost.matches(Regex("[A-Za-z0-9]+(?:[.-][A-Za-z0-9]+)*"))) { "Invalid reset link host" }
    namespace = "pe.edu.upc.saludya"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "pe.edu.upc.saludya"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["resetLinkHost"] = resetLinkHost
        buildConfigField("String", "RESET_LINK_HOST", "\"$resetLinkHost\"")
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"${providers.gradleProperty("saludya.apiBaseUrl").getOrElse("http://10.0.2.2:8080/")}\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"${providers.gradleProperty("saludya.apiBaseUrl").getOrElse("https://localhost/")}\"")
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Data Store
    implementation(libs.androidx.datastore.preferences)
}
