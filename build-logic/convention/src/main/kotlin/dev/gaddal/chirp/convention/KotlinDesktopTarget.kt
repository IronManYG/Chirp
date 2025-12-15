package dev.gaddal.chirp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the desktop target for Kotlin Multiplatform projects.
 *
 * This method adds a JVM target named "desktop" to the Kotlin Multiplatform extension,
 * setting the JVM target version to 17 for all its compilations. It ensures compatibility
 * with JVM 17 by specifying the appropriate compiler options.
 *
 * This function is intended for use within a Gradle project using the Kotlin Multiplatform plugin.
 */
internal fun Project.configureDesktopTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm("desktop") {
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }
            }
        }
    }
}