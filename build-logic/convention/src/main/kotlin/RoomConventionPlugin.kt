import androidx.room.gradle.RoomExtension
import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * A Gradle plugin to configure Room and Kotlin Symbol Processing (KSP) in a Kotlin Multiplatform project.
 *
 * This plugin streamlines the setup process for projects using Room as a database solution by:
 * - Applying the necessary Gradle plugins for Room and KSP integration.
 * - Configuring Room-related properties, such as the schema output directory.
 * - Declaring dependencies required for Room database usage across platforms, including Android and iOS.
 *
 * The RoomConventionPlugin is intended to support projects that require Room database features
 * in a Kotlin Multiplatform environment, ensuring convention-based configuration and simplifying schema management.
 */
class RoomConventionPlugin : Plugin<Project> {

    /**
     * Configures the given Gradle project with the Room Convention Plugin settings.
     *
     * This method applies necessary plugins and dependencies to set up the project
     * for usage with Room and Kotlin Symbol Processing (KSP). It also configures the
     * Room schema directory and specifies dependencies for Room runtime and compiler
     * for various platforms.
     *
     * @param target the Gradle project to which the plugin and its configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "commonMainApi"(libs.findLibrary("androidx-room-runtime").get())
                "commonMainApi"(libs.findLibrary("sqlite-bundled").get())
                "kspAndroid"(libs.findLibrary("androidx-room-compiler").get())
                "kspIosSimulatorArm64"(libs.findLibrary("androidx-room-compiler").get())
                "kspIosArm64"(libs.findLibrary("androidx-room-compiler").get())
                "kspIosX64"(libs.findLibrary("androidx-room-compiler").get())
                "kspDesktop"(libs.findLibrary("androidx-room-compiler").get())
            }
        }
    }
}