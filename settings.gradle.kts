rootProject.name = "re-svg"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
    includeBuild("./plugins/openrndr")
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
