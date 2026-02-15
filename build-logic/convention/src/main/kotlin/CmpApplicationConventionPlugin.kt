import dev.gaddal.chirp.convention.applyHierarchyTemplate
import dev.gaddal.chirp.convention.configureAndroidLibraryTarget
import dev.gaddal.chirp.convention.configureDesktopTarget
import dev.gaddal.chirp.convention.configureIosTargets
import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * A Gradle plugin to configure conventions for a Kotlin Multiplatform application utilizing Compose Multiplatform.
 *
 * The `CmpApplicationConventionPlugin` streamlines the setup of a Kotlin Multiplatform project by:
 * - Applying the necessary plugins for Kotlin Multiplatform, Compose, and serialization support.
 * - Configuring targets for Android, iOS, and desktop platforms.
 * - Adding platform-specific dependencies as required.
 * - Applying hierarchical source set configurations to enable code sharing across platforms.
 *
 * This plugin is designed to reduce boilerplate and enforce consistent configurations for Kotlin Multiplatform applications.
 */
class CmpApplicationConventionPlugin : Plugin<Project> {

    /**
     * Applies the CMP Application Convention Plugin to the specified Gradle project.
     *
     * This method configures the project for Kotlin Multiplatform development by:
     * - Applying the necessary plugins, such as Android, Kotlin Multiplatform, Jetpack Compose, and Kotlin Serialization.
     * - Setting up platform-specific targets for Android, iOS, and desktop environments by invoking configuration methods.
     * - Configuring dependencies required for project compilation and runtime.
     * - Applying a predefined source set hierarchy template for shared code organization.
     *
     * @param target the Gradle project to which the CMP Application Convention Plugin is applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidLibraryTarget()
            configureIosTargets()
            configureDesktopTarget()

            extensions.configure<KotlinMultiplatformExtension> {
                applyHierarchyTemplate()
            }

            dependencies {
                "androidMainImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}