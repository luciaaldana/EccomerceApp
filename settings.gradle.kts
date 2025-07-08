pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "EccomerceApp"
include(":app")

// Core modules
include(":core:model")
include(":core:navigation")
include(":core:ui")

// Domain modules
include(":domain:auth")
include(":domain:product")
include(":domain:cart")

// Data modules
include(":data:auth")
include(":data:product")
include(":data:cart")

// Feature modules
include(":feature:login")
include(":feature:register")
include(":feature:home")
include(":feature:cart")
include(":feature:profile")
 