package dev.gaddal.core.domain.util

/**
 * Represents a specialized exception type for data-related errors.
 *
 * The `DataErrorException` class is used to encapsulate an error of type `DataError`,
 * providing a structured way to handle and propagate data-related errors in an application.
 *
 * This exception can be thrown when a specific `DataError` is encountered, making it
 * easier to handle error scenarios in a consistent and predictable manner.
 *
 * @param error An instance of `DataError` representing the specific data-related error.
 */
class DataErrorException(
    val error: DataError
) : Exception()