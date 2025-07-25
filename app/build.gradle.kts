import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
    kotlin("kapt")
}

// 🧪 Leer local.properties
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

android {
    namespace = "com.luciaaldana.eccomerceapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.luciaaldana.eccomerceapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ✅ Agregar propiedades a BuildConfig
        buildConfigField("String", "API_KEY", "\"${localProperties["API_KEY"]}\"")
        buildConfigField("String", "RENDER_BASE_URL", "\"${localProperties["RENDER_BASE_URL"]}\"")
        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"${localProperties["CLOUDINARY_CLOUD_NAME"]}\"")
        buildConfigField("String", "CLOUDINARY_API_KEY", "\"${localProperties["CLOUDINARY_API_KEY"]}\"")
        buildConfigField("String", "CLOUDINARY_API_SECRET", "\"${localProperties["CLOUDINARY_API_SECRET"]}\"")
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

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    lint {
        abortOnError = false
        checkReleaseBuilds = false
        warningsAsErrors = false
        quiet = true
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:cloudinary"))
    implementation(project(":feature:home"))
    implementation(project(":feature:cart"))
    implementation(project(":feature:login"))
    implementation(project(":feature:register"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:productlist"))
    implementation(project(":data:auth"))
    implementation(project(":data:cart"))
    implementation(project(":data:product"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation("androidx.compose.foundation:foundation")
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    testImplementation(libs.bundles.test.core)
    testImplementation(libs.mockk)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    kaptTest(libs.hilt.compiler)
    kapt(libs.dagger.android.processor)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}