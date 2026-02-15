package dev.gaddal.chirp.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures Jetpack Compose build settings and dependencies for the provided Android module.
 *
 * This method enables the Compose build feature and sets up the necessary dependencies
 * for Jetpack Compose. It includes the Compose BOM (Bill of Materials) for managing
 * dependency versions and adds debug-specific dependencies for Compose UI tooling.
 *
 * @param applicationExtension the Android ApplicationExtension instance used to define Android-specific build configurations
 */
internal fun Project.configureAndroidCompose(
    applicationExtension: ApplicationExtension
) {
    with(applicationExtension) {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "testImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}