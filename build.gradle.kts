import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.beryx.runtime.JPackageTask
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runtime)
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
}

application {
    mainClass.set("MainKt")
}

tasks {
    withType<ShadowJar> {
        manifest{
            attributes["Main-Class"] = "MainKt"
            attributes["Implementation-Version"] = project.version
        }
        minimize {
            exclude(dependency("org.openrndr:openrndr-gl3:.*"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
        }
    }
    withType<JPackageTask> {
        doLast {
            val destPath = if(OperatingSystem.current().isMacOsX) "build/jpackage/re-svg.app/Contents/Resources/data" else "build/jpackage/re-svg/data"

            copy {
                from("data") {
                    include("**/*")
                }
                into(destPath)
            }
        }
    }
}

runtime {
    jpackage {
        imageName = "re-svg"
        imageOptions = listOf("--icon", "data/icon.ico")
        skipInstaller = true
        if (OperatingSystem.current().isMacOsX) {
            jvmArgs.add("-XstartOnFirstThread")
            jvmArgs.add("-Duser.dir=${"$"}APPDIR/../Resources")
        }
    }
    options.set(listOf("--strip-debug", "--compress", "1", "--no-header-files", "--no-man-pages"))
    modules.set(listOf("jdk.unsupported", "java.management", "java.desktop"))
}

task("createExecutable") {
    group = "build"
    dependsOn("jpackage")
}
