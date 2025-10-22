package dev.gaddal.core.domain.util

/**
 * Represents a sealed interface for handling results in a type-safe manner.
 *
 * This interface is designed to encapsulate the outcome of operations that may either succeed
 * with a value or fail due to an error. It provides a common structure for modeling and managing
 * success and failure scenarios, aiding in the separation of concerns and streamlined error handling.
 *
 * @param D The type of the success data returned when the operation is successful.
 * @param E The type of the error that occurs when the operation fails. This must extend the `Error` interface.
 */
sealed interface Result<out D, out E: Error> {
    /**
     * Represents a successful result in a `Result` wrapper.
     *
     * This class encapsulates the success case of an operation where the expected
     * data is returned. The generic type parameter `D` represents the type of the
     * successful data.
     *
     * The `Success` class is a subtype of the `Result` interface, specifically
     * implementing the success variant (`Result<D, Nothing>`). It is primarily used
     * in domain modeling to signify successful outcomes and to enable safe handling
     * of success and failure states in a functional programming style.
     *
     * @param D The type of the data contained in the success result.
     * @property data The successful data outcome of type `D`.
     */
    data class Success<out D>(val data: D): Result<D, Nothing>
    /**
     * Represents a failure result type in the context of a result-handling mechanism.
     *
     * This class is a part of the `Result` sealed interface and is used to denote
     * unsuccessful operations or processes where an error has occurred. The error
     * information is encapsulated in the provided `error` property, which must conform
     * to the `Error` interface.
     *
     * @param E The type of error, extending the `Error` interface.
     * @property error The specific error instance that describes the failure.
     */
    data class Failure<out E: Error>(val error: E): Result<Nothing, E>
}

/**
 * Transforms the success value of a [Result] using the provided mapping function while preserving the error.
 *
 * @param map A function to transform the success value of type [T] into a new type [R].
 * @return A new [Result] containing the transformed success value of type [R] if this is a success result,
 *         or the original error if this is a failure result.
 */
inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Failure -> Result.Failure(error)
        is Result.Success -> Result.Success(map(this.data))
    }
}

/**
 * Executes the specified action if the result represents a success.
 *
 * If this instance of `Result` holds a successful result, the provided action will
 * be invoked with the success data. If this instance represents a failure, the action
 * is not executed.
 *
 * @param action A lambda function to be executed if the result is successful, receiving the success data as an argument.
 * @return The same instance of `Result` on which the method was invoked, unchanged.
 */
inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(this.data)
            this
        }
    }
}

/**
 * Executes the provided action if the current instance is a `Failure` result.
 *
 * @param action A lambda function to be executed with the error value if this is a `Failure`.
 * @return The current instance of `Result<T, E>` to allow for method chaining.
 */
inline fun <T, E: Error> Result<T, E>.onFailure(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

/**
 * Transforms the current `Result` into an `EmptyResult`, discarding any success data while retaining the error type.
 *
 * @return An `EmptyResult` containing the same error type from the original `Result`.
 */
fun <T, E: Error> Result<T, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

/**
 * A type alias that represents a `Result` structure where the success value is `Unit`.
 *
 * This alias simplifies the definition of results where the success outcome does not carry any data,
 * but instead only signifies the operation's completion. The error type is still parameterized and
 * must implement the `Error` interface.
 *
 * Common use case scenarios:
 * - Representing success or failure in operations where no meaningful data needs to be returned on success.
 * - Standardizing result structures in cases where only error handling matters.
 *
 * @param E The type of the error, which must extend the `Error` interface.
 */
typealias EmptyResult<E> = Result<Unit, E>