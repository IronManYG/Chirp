package dev.gaddal.chirp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures Kotlin and Android build settings for a given project module.
 *
 * This method sets up the compile SDK version, minimum SDK version, Java compatibility options,
 * and enables core library desugaring. It also applies additional Kotlin-specific compiler configurations.
 * A dependency for core library desugaring is added to the project.
 *
 * @param commonExtension the Android CommonExtension instance used to define shared Android build configurations
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }

        configureKotlin()

        dependencies {
            "coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
        }
    }
}

/**
 * Configures Kotlin compilation settings for the Gradle project.
 *
 * This method ensures that all Kotlin compilation tasks are set to use the
 * JVM target `17`. Additionally, it adds the required compiler argument
 * to opt into the `kotlinx.coroutines.ExperimentalCoroutinesApi` feature.
 *
 * This configuration is applied to all Kotlin compile tasks within the project.
 */
internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)

            freeCompilerArgs.add(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}