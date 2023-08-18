plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.10"
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
