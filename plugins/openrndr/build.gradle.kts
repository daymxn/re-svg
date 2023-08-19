plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
}

group = "com.daymxn"
version = "1.0.0"

gradlePlugin {
    plugins {
        register("openrndr") {
            id = "openrndr"
            implementationClass = "plugins.Openrndr"
        }
    }
}
