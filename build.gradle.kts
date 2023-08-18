import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    id("openrndr")
}

group = "com.daymxn"
version = "1.0.0"

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(kotlin("stdlib-jdk8"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
}
