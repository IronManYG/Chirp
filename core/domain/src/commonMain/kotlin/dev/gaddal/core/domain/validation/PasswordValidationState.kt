package dev.gaddal.core.domain.validation

/**
 * Represents the validation state of a password based on defined criteria:
 * minimum length, presence of at least one digit, and presence of at least one uppercase letter.
 *
 * @param hasMinLength Indicates whether the password meets the minimum length requirement.
 * @param hasDigit Indicates whether the password contains at least one digit.
 * @param hasUppercase Indicates whether the password contains at least one uppercase letter.
 */
data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasUppercase: Boolean = false
) {
    /**
     * Indicates whether the password meets all the required validation criteria.
     *
     * This property evaluates to `true` only if the following conditions are satisfied:
     * - The password has a minimum required length.
     * - The password contains at least one numeric digit.
     * - The password contains at least one uppercase character.
     *
     * Used to determine the validity of a password based on the `PasswordValidationState`.
     */
    val isValidPassword: Boolean
        get() = hasMinLength && hasDigit && hasUppercase
}