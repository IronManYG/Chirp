import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A Gradle plugin that sets up a library module for use with Jetpack Compose in a Kotlin Multiplatform project.
 *
 * This plugin applies necessary conventions and dependencies to enable Compose functionality
 * for Kotlin Multiplatform library modules. Specifically, it:
 * - Applies Compose-related plugins, such as `org.jetbrains.kotlin.plugin.compose` and `org.jetbrains.compose`.
 * - Configures common dependencies required for Compose features, including UI, Foundation, Material 3,
 *   and Material Icons libraries.
 *
 * By applying this plugin, Kotlin Multiplatform projects gain predefined Compose configurations and
 * dependencies, streamlining the setup process and ensuring consistency across modules.
 */
class CmpLibraryConventionPlugin: Plugin<Project> {

    /**
     * Applies the CMP Library Convention Plugin configuration to the specified Gradle project.
     *
     * This method applies required plugins and dependencies to set up the project as a Kotlin Multiplatform
     * library with Jetpack Compose integration. The configuration includes adding specific libraries
     * for common source sets to support Compose UI development.
     *
     * @param target the Gradle project to which the plugin and configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.gaddal.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material-icons-core").get())

                "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}