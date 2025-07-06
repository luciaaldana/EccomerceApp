// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.ksp) apply false
}

subprojects {
    // Aplica solo a módulos con tests unitarios
    plugins.withId("com.android.library") {
        apply(plugin = "org.jetbrains.kotlinx.kover")
    }
    plugins.withId("com.android.application") {
        apply(plugin = "org.jetbrains.kotlinx.kover")
    }
}
