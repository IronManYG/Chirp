import dev.gaddal.chirp.convention.configureKotlinMultiplatform
import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A Gradle plugin to configure a Kotlin Multiplatform (KMP) library project with standardized conventions.
 *
 * This plugin simplifies the setup of a KMP library by automating the following tasks:
 * - Applying necessary Gradle plugins for Kotlin Multiplatform, Android libraries, and Kotlin Serialization.
 * - Configuring Kotlin Multiplatform project targets and dependencies.
 * - Ensuring consistent dependency management by adding libraries like kotlinx-serialization-json and kotlin-test.
 *
 * The KmpLibraryConventionPlugin is designed to support building shared Kotlin codebases for multiple platforms,
 * including Android, iOS, and Desktop, while enforcing project-level conventions to ensure compatibility
 * and maintainability.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {

    /**
     * Configures the given Gradle project to align with the Kotlin Multiplatform Library conventions.
     *
     * This method applies essential Gradle plugins and sets up dependencies specific to Kotlin Multiplatform projects.
     * It incorporates support for Android, Kotlin Multiplatform, and Kotlin Serialization. Additionally, it invokes
     * project-specific configurations for a Kotlin Multiplatform setup and declares dependencies for common source sets.
     *
     * @param target the Gradle project to which the Kotlin Multiplatform Library conventions are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureKotlinMultiplatform()

            dependencies {
                "commonMainImplementation"(libs.findLibrary("kotlinx-serialization-json").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}