import dev.gaddal.chirp.convention.applyHierarchyTemplate
import dev.gaddal.chirp.convention.configureAndroidTarget
import dev.gaddal.chirp.convention.configureDesktopTarget
import dev.gaddal.chirp.convention.configureIosTargets
import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * A Gradle plugin that sets up Kotlin Multiplatform configurations for projects utilizing Android, iOS and Desktop targets,
 * with additional Jetpack Compose features enabled for Android.
 *
 * This plugin applies necessary build conventions and dependencies for all platforms
 * to streamline the development process in a Kotlin Multiplatform project. Specifically, it:
 * - Configures the Kotlin Multiplatform Android target with Java 17 compatibility.
 * - Sets up iOS targets (`iosX64`, `iosArm64`, `iosSimulatorArm64`) with static frameworks for interoperability.
 * - Configures Desktop target with JVM 17 compatibility.
 * - Applies Compose-specific plugins and dependencies for Android.
 * - Uses a centralized dependency management mechanism (version catalog).
 *
 * By utilizing this plugin, projects gain predefined, reusable configurations for Android, iOS and Desktop,
 * reducing boilerplate and ensuring adherence to project standards.
 */
class CmpApplicationConventionPlugin : Plugin<Project> {

    /**
     * Applies the CMP Application Convention Plugin configuration to the specified Gradle project.
     *
     * This method applies required plugins and configures the project for Android, iOS, and desktop targets.
     * It integrates several plugins, including the Kotlin Multiplatform plugin and Compose-related plugins.
     * It applies source set hierarchy templates for consistent project structure.
     * It also sets dependencies for Compose development.
     *
     * @param target the Gradle project to which the plugin and configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.gaddal.convention.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidTarget()
            configureIosTargets()
            configureDesktopTarget()

            extensions.configure<KotlinMultiplatformExtension> {
                applyHierarchyTemplate()
            }

            dependencies {
                "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}