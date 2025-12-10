package dev.gaddal.chat.data.di

import dev.gaddal.chat.data.lifecycle.AppLifecycleObserver
import dev.gaddal.chat.data.network.ConnectionErrorHandler
import dev.gaddal.chat.data.network.ConnectivityObserver
import dev.gaddal.chat.data.notification.FirebasePushNotificationService
import dev.gaddal.chat.database.DatabaseFactory
import dev.gaddal.chat.domain.notification.PushNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Defines the `platformChatDataModule` as a Koin module for dependency injection.
 *
 * This module provides platform-specific dependencies required for the chat application,
 * including database creation, connection error handling, network connectivity observation,
 * application lifecycle management, and push notification services.
 *
 * Registered components:
 * - `DatabaseFactory`: Factory for creating and managing the local database.
 * - `ConnectionErrorHandler`: Handles network-related errors and connection state mapping.
 * - `ConnectivityObserver`: Monitors the network connectivity status.
 * - `AppLifecycleObserver`: Observes the application's lifecycle to determine foreground state (not needed on desktop platforms but maintained since code is shared between mobile and desktop platforms so it needs to be injected).
 * - `FirebasePushNotificationService`: Manages push notification functionality (specific to Firebase),
 *   bound to the `PushNotificationService` interface (not needed on desktop platforms but maintained since code is shared between mobile and desktop platforms so it needs to be injected).
 */
actual val platformChatDataModule = module {
    singleOf(::DatabaseFactory)
    singleOf(::ConnectionErrorHandler)
    singleOf(::ConnectivityObserver)
    singleOf(::AppLifecycleObserver)
    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}