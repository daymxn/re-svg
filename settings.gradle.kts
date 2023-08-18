rootProject.name = "re-svg"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
    includeBuild("plugins")
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
