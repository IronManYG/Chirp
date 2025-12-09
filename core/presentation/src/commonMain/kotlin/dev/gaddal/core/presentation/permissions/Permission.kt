package dev.gaddal.core.presentation.permissions

/**
 * Represents the various permissions that can be requested or managed within the application.
 *
 * This enum is used in conjunction with [PermissionController] to determine whether a specific
 * permission has been granted, denied, or requires further user action.
 *
 * @see PermissionController
 * @see PermissionState
 */
enum class Permission {
    NOTIFICATIONS
}