import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A Gradle plugin that configures a Kotlin Multiplatform feature module with dependencies and conventions
 * tailored for projects using Jetpack Compose and Koin for dependency injection.
 *
 * This plugin is intended to be used within a feature module in a Kotlin Multiplatform project. It sets up
 * necessary plugins and dependencies for Compose and Koin integration. Specifically, it:
 * - Applies the `CmpLibraryConventionPlugin` as a foundational library configuration.
 * - Declares dependencies required for core presentation, design system, and Jetpack Compose.
 * - Adds Koin libraries to enable dependency injection in both common and Android source sets.
 * - Defines additional Compose-related libraries for features like lifecycle, navigation, and state management.
 */
class CmpFeatureConventionPlugin: Plugin<Project> {

    /**
     * Configures the specified Gradle project with CMP Feature Convention Plugin settings.
     *
     * This method applies necessary plugins and dependencies to set up the project as a specific Kotlin Multiplatform
     * feature module with Jetpack Compose integration. The setup includes applying conventions and adding required
     * libraries for common and Android-specific source sets to support Compose-based development.
     *
     * @param target the Gradle project to which the plugin and its configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.gaddal.convention.cmp.library")
            }

            dependencies {
                "commonMainImplementation"(project(":core:presentation"))
                "commonMainImplementation"(project(":core:designsystem"))

                "commonMainImplementation"(platform(libs.findLibrary("koin-bom").get()))
                "androidMainImplementation"(platform(libs.findLibrary("koin-bom").get()))

                "commonMainImplementation"(libs.findLibrary("koin-compose").get())
                "commonMainImplementation"(libs.findLibrary("koin-compose-viewmodel").get())

                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-compose").get())

                "commonMainImplementation"(libs.findLibrary("jetbrains-lifecycle-viewmodel-savedstate").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-savedstate").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-bundle").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-navigation").get())

                "androidMainImplementation"(libs.findLibrary("koin-android").get())
                "androidMainImplementation"(libs.findLibrary("koin-androidx-compose").get())
                "androidMainImplementation"(libs.findLibrary("koin-androidx-navigation").get())
                "androidMainImplementation"(libs.findLibrary("koin-core-viewmodel").get())
            }
        }
    }
}