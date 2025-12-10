package dev.gaddal.chirp.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures Kotlin Multiplatform for the current Gradle project.
 *
 * This method sets up the Kotlin Multiplatform plugin and adjusts its settings specific
 * to an iOS and Android target configuration. It handles the following tasks:
 *
 * - Configures the Android target by invoking `configureAndroidTarget`.
 * - Configures the Desktop target by invoking `configureDesktopTarget`.
 * - Sets the namespace for the project based on the project path, utilizing `pathToPackageName`.
 * - Configures iOS targets (iosX64, iosArm64, iosSimulatorArm64) and their binary frameworks,
 *   setting their base name using `pathToFrameworkName`.
 * - Adjusts Kotlin compiler options, enabling experimental features and opt-ins
 *   such as `kotlin.RequiresOptIn` and `kotlin.time.ExperimentalTime`.
 */
internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<LibraryExtension> {
        namespace = this@configureKotlinMultiplatform.pathToPackageName()
    }

    configureAndroidTarget()
    configureDesktopTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
            }
        }

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}