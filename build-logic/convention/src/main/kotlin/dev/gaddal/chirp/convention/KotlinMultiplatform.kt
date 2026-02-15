package dev.gaddal.chirp.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the Kotlin Multiplatform setup for the current Gradle project.
 *
 * This function defines platform-specific targets and configurations for Android, iOS, and Desktop
 * within a Kotlin Multiplatform project. It includes the following configuration steps:
 *
 * - Configures the Android library target with compile SDK, minimum SDK, namespace, and experimental properties.
 * - Configures the desktop target with JVM 17 compatibility.
 * - Defines iOS targets (X64, ARM64, and Simulator ARM64) and sets the base name for their binary frameworks.
 * - Applies a predefined source set hierarchy template to align with Kotlin Multiplatform conventions.
 * - Adds compiler options to enable expect/actual classes and experimental features such as time APIs.
 *
 * The method is designed to streamline the setup process for Kotlin Multiplatform projects, ensuring
 * consistent configurations for all applicable platforms.
 */
internal fun Project.configureKotlinMultiplatform() {
    configureAndroidLibraryTarget()
    configureDesktopTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
            compileSdk = 36
            minSdk = 26
            namespace = pathToPackageName()
            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        }

        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
            }
        }

        applyHierarchyTemplate()

        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}