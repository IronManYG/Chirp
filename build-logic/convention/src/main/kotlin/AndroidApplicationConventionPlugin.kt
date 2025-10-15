import com.android.build.api.dsl.ApplicationExtension
import dev.gaddal.chirp.convention.configureKotlinAndroid
import dev.gaddal.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * A Gradle plugin that configures an Android application module with custom conventions.
 *
 * This plugin applies the `com.android.application` plugin and configures the Android application
 * extension with project-specific settings such as namespace, default configuration,
 * packaging options, and build types. It also sets up Kotlin and compile-time configurations
 * using specific project dependencies and JDK versions.
 */
class AndroidApplicationConventionPlugin: Plugin<Project> {

    /**
     * Applies the Android application convention plugin configuration to the specified Gradle project.
     *
     * @param target the Gradle project to which the plugin and configurations are applied
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                namespace = "dev.gaddal.chirp"

                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                configureKotlinAndroid(this)
            }
        }
    }
}