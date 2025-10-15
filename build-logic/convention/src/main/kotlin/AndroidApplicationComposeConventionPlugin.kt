import com.android.build.api.dsl.ApplicationExtension
import dev.gaddal.chirp.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * A Gradle plugin that applies and configures Compose-specific settings for an Android application module.
 *
 * This plugin extends the functionality of the Android application convention plugin by including
 * default configurations for Jetpack Compose. It ensures that the Compose build features are enabled
 * and sets up required dependencies for Compose, such as the BOM (Bill of Materials) and development tools.
 *
 * It is designed to simplify the configuration process for projects using Jetpack Compose.
 */
class AndroidApplicationComposeConventionPlugin: Plugin<Project> {

    /**
     * Applies the Android application compose convention plugin configuration to the specified Gradle project.
     *
     * This method applies required plugins and configures the Android application extension by enabling
     * compose-specific settings and dependencies.
     *
     * @param target the Gradle project to which the plugin and configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.gaddal.convention.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}