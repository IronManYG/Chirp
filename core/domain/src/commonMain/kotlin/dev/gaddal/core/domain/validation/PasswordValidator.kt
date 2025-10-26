package dev.gaddal.core.domain.validation

/**
 * Provides functionality for validating passwords based on predefined criteria.
 *
 * The validation checks for the following conditions:
 * - A minimum password length.
 * - The presence of at least one numeric digit.
 * - The presence of at least one uppercase character.
 */
object PasswordValidator {

    /**
     * Defines the minimum required length for a valid password.
     *
     * Used during password validation to ensure that the password satisfies
     * the length requirement as part of the password validation criteria.
     *
     * The password is considered valid if its length is greater than or equal
     * to this value.
     */
    private const val MIN_PASSWORD_LENGTH = 9

    /**
     * Validates the given password against predefined criteria: minimum length, presence of at least one digit,
     * and presence of at least one uppercase letter. Returns a `PasswordValidationState` indicating the validation results.
     *
     * @param password The password to validate.
     * @return A `PasswordValidationState` containing the validation results for the password.
     */
    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
            hasDigit = password.any { it.isDigit() },
            hasUppercase = password.any { it.isUpperCase() }
        )
    }
}