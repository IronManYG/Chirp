package dev.gaddal.chirp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures Gradle settings specific to Android library modules.
 *
 * This method adds a dependency for core library desugaring, enabling the support
 * of Java 8+ API desugaring in Android projects. It utilizes the version and library
 * details defined in the custom version catalog (`libs`).
 *
 * This configuration is intended to be used for Android library modules within
 * modularized Gradle projects.
 */
internal fun Project.configureAndroidLibraryTarget() {
    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
    }
}