package dev.gaddal.chirp.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Provides access to the custom version catalog named "libs" defined in the Gradle project.
 *
 * This property is an extension for the Project class and allows retrieving dependencies,
 * versions, and libraries defined in the shared "libs.versions.toml" file.
 *
 * It is commonly used to look up versioned dependencies and project configurations
 * in modularized Gradle projects.
 */
val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")