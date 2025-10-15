package dev.gaddal.chirp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the Kotlin Multiplatform Android target for the Gradle project.
 *
 * This method sets up the Android target within the Kotlin Multiplatform plugin and configures
 * the Kotlin compiler options to use Java 17 (JVM Target 17) for the Android target compilation.
 * This configuration ensures that the Android module adheres to the specified JVM target version.
 *
 * It utilizes the `androidTarget` function provided by Kotlin Multiplatform, and applies
 * experimental Kotlin Gradle Plugin API settings where necessary.
 */
internal fun Project.configureAndroidTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        androidTarget {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
}