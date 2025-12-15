package dev.gaddal.core.data.di

import dev.gaddal.core.data.auth.createDataStore
import dev.gaddal.core.data.preferences.DataStoreThemePreferences
import dev.gaddal.core.domain.preferences.ThemePreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides a Koin module specifically for platform-specific core data dependencies.
 *
 * This module includes the following bindings:
 * - A singleton instance of a `DataStore<Preferences>` created using `createDataStore`, which is
 *   responsible for providing a storage solution for managing preferences.
 * - A singleton instance of `HttpClientEngine` using `OkHttp`, which is essential for platform-specific
 *   HTTP operations.
 * - A singleton instance of `ThemePreferences` using `DataStoreThemePreferences`, which manages theme-related
 *   preferences.
 *
 * The module abstracts platform differences in data storage and HTTP client configuration, offering a unified
 * and reusable dependency injection setup for core data operations across platforms.
 */
actual val platformCoreDataModule = module {
    single { createDataStore() }
    single<HttpClientEngine> { OkHttp.create() }
    singleOf(::DataStoreThemePreferences) bind ThemePreferences::class
}