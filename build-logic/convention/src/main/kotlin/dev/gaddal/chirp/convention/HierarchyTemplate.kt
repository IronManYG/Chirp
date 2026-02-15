@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package dev.gaddal.chirp.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

/**
 * Represents a configuration template for setting up a Kotlin project hierarchy
 * in a Gradle build environment. Defines a structure for organizing source sets,
 * targets, and their corresponding configurations.
 *
 * This hierarchy template configures multiple groups to separate concerns by
 * platforms, such as Android, iOS, JVM, and Native. Each group includes specific
 * settings for the associated targets, such as enabling Android target configurations,
 * iOS frameworks, or common JVM settings. Nested groups represent more granular
 * configurations for shared or platform-specific code.
 *
 * The structure includes:
 * - A source set tree for common and test sources.
 * - Group definitions for `mobile` and `native`.
 * - Sub-groups for targets like `apple` (including `ios` and `macos`) within the `native` group.
 */
private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        group("mobile") {
            withAndroidTarget()
            group("ios") {
                withIos()
            }
        }

        group("native") {
            withNative()

            group("apple") {
                withApple()

                group("ios") {
                    withIos()
                }

                group("macos") {
                    withMacos()
                }
            }
        }
    }
}

/**
 * Applies the hierarchy template configuration to the Kotlin Multiplatform project.
 *
 * This method utilizes the predefined hierarchy template to configure source set relationships
 * and ensure a consistent structure for shared code between different platforms within the
 * Kotlin Multiplatform project.
 *
 * It is typically used to streamline the setup process for projects where hierarchical
 * source set configurations need to be applied, enabling efficient reusability of code across
 * multiple targets.
 */
fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}