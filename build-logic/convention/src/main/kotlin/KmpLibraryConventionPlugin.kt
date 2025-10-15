import com.android.build.api.dsl.LibraryExtension
import dev.gaddal.chirp.convention.configureKotlinAndroid
import dev.gaddal.chirp.convention.configureKotlinMultiplatform
import dev.gaddal.chirp.convention.libs
import dev.gaddal.chirp.convention.pathToResourcePrefix
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * A Gradle plugin that configures a Kotlin Multiplatform library module with Android and iOS targets.
 *
 * This plugin applies common conventions, dependencies, and settings for Kotlin Multiplatform library modules
 * in a Gradle project. Specifically, it:
 * - Applies required Gradle plugins, including the Android library plugin, Kotlin Multiplatform plugin,
 *   and Kotlin serialization plugin.
 * - Configures the Kotlin Multiplatform extension with settings specific to Android and iOS targets,
 *   using the `configureKotlinMultiplatform` helper function.
 * - Configures Android-specific settings for the library module via the `configureKotlinAndroid` helper function.
 * - Sets a consistent resource prefix for Android resources derived from the project's path.
 * - Configures experimental properties to support Android resource usage in command-line builds
 *   and iOS simulator deployments.
 * - Declares common dependencies for the `commonMain` and `commonTest` source sets, such as Kotlin Serialization
 *   and Kotlin Test libraries.
 */
class KmpLibraryConventionPlugin: Plugin<Project> {

    /**
     * Applies the `KmpLibraryConventionPlugin` configuration to the specified Gradle project.
     *
     * This method sets up the project to use Kotlin Multiplatform and Android targets, configuring
     * necessary plugins and dependencies. It also defines custom build settings, such as resource prefixes
     * and experimental properties required for Kotlin Multiplatform projects with Android and iOS targets.
     *
     * @param target the Gradle project to which this plugin's configuration is applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureKotlinMultiplatform()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                resourcePrefix = this@with.pathToResourcePrefix()

                // Required to make debug build of app run in iOS simulator
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("kotlinx-serialization-json").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}