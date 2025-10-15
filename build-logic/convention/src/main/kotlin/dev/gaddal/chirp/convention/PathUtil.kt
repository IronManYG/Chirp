package dev.gaddal.chirp.convention

import org.gradle.api.Project
import java.util.Locale

/**
 * Converts the Gradle project path to a corresponding package name.
 *
 * This method transforms the project path by replacing colons (':') with dots ('.'),
 * converting the resulting string to lowercase, and prepending it with `dev.gaddal`.
 *
 * @return the package name derived from the project path
 */
fun Project.pathToPackageName(): String {
    val relativePackageName = path
        .replace(':', '.')
        .lowercase()

    return "dev.gaddal$relativePackageName"
}

/**
 * Generates a formatted resource prefix string based on the Gradle project's path.
 *
 * The method replaces colons in the project path with underscores, converts the string
 * to lowercase, removes the leading colon, and appends an underscore at the end to create
 * a consistent prefix for resources.
 *
 * @return the formatted resource prefix derived from the project's path
 */
fun Project.pathToResourcePrefix(): String {
    return path
        .replace(':', '_')
        .lowercase()
        .drop(1) + "_"
}

/**
 * Converts the project path into a framework-compatible name by transforming it into
 * a camel-case format. The method splits the path into parts using common delimiters
 * such as ":", "-", "_", and whitespace, then capitalizes the first character of each part
 * and concatenates them. This is commonly used for generating framework names
 * from project paths in a structured way.
 *
 * @return a string representing the camel-case formatted framework name based on the project path
 */
fun Project.pathToFrameworkName(): String {
    val parts = this.path.split(":", "-", "_", " ")
    return parts.joinToString("") { part ->
        part.replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
}