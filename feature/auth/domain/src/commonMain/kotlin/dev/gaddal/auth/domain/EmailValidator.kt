package dev.gaddal.auth.domain

/**
 * Provides utility functions for validating email addresses.
 *
 * This object includes a method to validate email strings using a predefined
 * regular expression pattern. The regular expression ensures that the email
 * string matches common email formats.
 */
object EmailValidator {

    /**
     * Regular expression pattern used to validate email addresses.
     * Defines the structure of a valid email address by ensuring it consists of:
     * - A combination of alphanumeric characters, dots, underscores, percent symbols, plus signs, or hyphens before the "@" symbol.
     * - A domain name consisting of alphanumeric characters and hyphens separated by dots.
     * - A top-level domain of at least two alphabetic characters.
     */
    private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"

    /**
     * Validates the provided email string against a predefined email pattern.
     *
     * @param email The email string to be validated.
     * @return `true` if the email matches the pattern; `false` otherwise.
     */
    fun validate(email: String): Boolean {
        return EMAIL_PATTERN.toRegex().matches(email)
    }
}