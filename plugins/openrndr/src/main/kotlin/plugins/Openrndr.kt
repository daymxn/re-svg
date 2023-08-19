package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.getByType
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

inline fun <reified T> Project.provideProperty(property: String) = provider {
    findProperty(property) as? T
}

abstract class Openrndr: Plugin<Project> {
    val orxFeatures = setOf(
            "orx-camera",
            "orx-color",
            "orx-compositor",
            "orx-fx",
            "orx-gui",
            "orx-image-fit",
            "orx-no-clear",
            "orx-noise",
            "orx-olive",
            "orx-panel",
            "orx-shade-styles",
            "orx-shapes",
            "orx-video-profiles",
            "orx-view-box",
    )

    val architecture: String
        get() = DefaultNativePlatform("current").architecture.name

    val Project.targetPlatform: String
        get() {
            val supported = listOf("windows", "macos", "linux-x64", "linux-arm64")
            val target = project.provideProperty<String>("targetPlatform").get()

            return target.takeIf { it in supported } ?: throw IllegalArgumentException("Target platform not supported: $target")
        }

    val os: String
        get() {
            val os = OperatingSystem.current()
            return when {
                os.isWindows -> "windows"
                os.isMacOsX -> when(architecture) {
                    "aarch64", "arm-v8" -> "macos-arm64"
                    else -> "macos"
                }
                os.isLinux -> when(architecture) {
                    "x86-64" -> "linux-x64"
                    "aarch64" -> "linux-arm64"
                    else -> throw IllegalArgumentException("Architecture not supported: $architecture")
                }
                else -> throw IllegalArgumentException("OS not supported: ${os.name}")
            }
        }

    override fun apply(project: Project) {
        val versionCatalog = project.extensions.getByType<VersionCatalogsExtension>().named("libs")
        val openrndrVersion = versionCatalog.findVersion("openrndr").get()
        val orxVersion = versionCatalog.findVersion("orx").get()

        val os = if(project.hasProperty("targetPlatform")) project.targetPlatform else os


        val mainDeps = listOf("gl3", "openal", "application", "svg", "animatable", "extensions", "filter", "dialogs")
        val nativeDeps = listOf("gl3", "openal")

        val implementation = project.configurations.getByName("implementation")
        val runtimeOnly = project.configurations.getByName("runtimeOnly")

        val main = mainDeps.map {
            project.dependencies.create("org.openrndr:openrndr-$it:$openrndrVersion")
        }
        val native = nativeDeps.map {
            project.dependencies.create("org.openrndr:openrndr-$it-natives-$os:$openrndrVersion")
        }
        val features = orxFeatures.map {
            project.dependencies.create("org.openrndr.extra:$it:$orxVersion")
        }

        implementation.dependencies.addAll(main + native + features)

        if(architecture != "arm-v8") {
            implementation.dependencies.add(
                    project.dependencies.create("org.openrndr:openrndr-ffmpeg:$openrndrVersion")
            )
            runtimeOnly.dependencies.add(
                    project.dependencies.create("org.openrndr:openrndr-ffmpeg-natives-$os:$openrndrVersion")
            )
        }

    }
}
