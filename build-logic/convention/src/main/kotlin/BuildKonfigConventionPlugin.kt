import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import dev.gaddal.chirp.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * A Gradle plugin for configuring BuildKonfig in a project.
 *
 * This plugin applies the `com.codingfeline.buildkonfig` plugin and sets up the `BuildKonfigExtension`
 * for generating build configuration at compile time. It facilitates the declaration of build configuration
 * fields with values that can be injected during the build process, such as API keys or other sensitive data.
 *
 * The package name for the generated configuration is derived from the project's Gradle path, ensuring
 * consistency across modules. It retrieves configuration values from the `local.properties` file, enabling
 * secure and flexible management of sensitive runtime configuration.
 */
class BuildKonfigConventionPlugin : Plugin<Project> {

    /**
     * Applies the BuildKonfigConventionPlugin settings to the specified Gradle project.
     *
     * This method configures the project with the BuildKonfig plugin and sets up BuildKonfigExtension properties.
     * It defines a default configuration for the BuildKonfig plugin by specifying the `packageName` and
     * adding a build config field for `API_KEY` that is fetched from `local.properties`.
     *
     * @param target the Gradle project to which the BuildKonfig plugin and configuration are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()
                defaultConfigs {
                    val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
                        .getProperty("API_KEY")
                        ?: throw IllegalStateException(
                            "Missing API_KEY property in local.properties"
                        )
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                }
            }
        }
    }
}