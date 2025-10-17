package dev.gaddal.core.domain.util

/**
 * Represents a generic interface for defining error types in the domain layer.
 *
 * This interface is designed to provide a shared contract for representing different kinds of errors
 * that can be used across various domain-level operations or result-handling mechanisms. Implementations
 * of this interface can encapsulate specific error information, such as localized or remote errors.
 *
 * Common usage scenarios include:
 * - Representing error types for failure cases in a result structure.
 * - Differentiating between various error sources (e.g., local vs remote).
 * - Providing a base type for errors in domain-oriented programming.
 */
interface Error