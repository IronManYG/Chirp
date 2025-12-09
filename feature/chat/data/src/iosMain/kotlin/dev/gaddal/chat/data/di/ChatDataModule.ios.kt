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
 * A module that provides platform-specific dependencies for handling chat-related database
 * operations on the iOS platform.
 *
 * This module configures singleton instances for database access, lifecycle monitoring, and connection error handling:
 * - `DatabaseFactory`: A factory class for initializing the `ChirpChatDatabase` using Room database builder.
 * - `AppLifecycleObserver`: A class that monitors application lifecycle state (foreground/background).
 * - `ConnectivityObserver`: A class that monitors network connectivity status.
 * - `ConnectionErrorHandler`: A class that handles connection errors and retries.
 *
 * This module ensures that platform-specific configurations required for database operations, lifecycle monitoring,
 * and network connectivity tracking are properly set up for the iOS platform.
 *
 * Dependencies:
 * - `DatabaseFactory`: Initializes the `ChirpChatDatabase` with iOS document directory path.
 * - `AppLifecycleObserver`: Tracks application foreground/background state using iOS notifications.
 * - `ConnectivityObserver`: Monitors network connectivity status using iOS reachability APIs.
 * - `ConnectionErrorHandler`: Handles connection errors and retries using iOS reachability APIs
 * - `FirebasePushNotificationService`: Handles push notifications using Firebase Cloud Messaging (FCM) for iOS.
 *
 * Usage Context:
 * - This Koin module should be included in an application's dependency injection setup,
 *   allowing the database factory, lifecycle services, connectivity observer, connection error handler, and push notification service to be injected wherever needed.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory() }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}