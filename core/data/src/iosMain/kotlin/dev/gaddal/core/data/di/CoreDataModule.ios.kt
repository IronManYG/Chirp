package dev.gaddal.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.gaddal.core.data.auth.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/**
 * Koin module that provides the iOS-specific implementation of [HttpClientEngine] and [DataStore].
 *
 * This module configures [Darwin] as the HTTP client engine for the iOS platform and provides
 * a platform-specific implementation of [DataStore] for preferences storage.
 */
actual val platformCoreDataModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single<DataStore<Preferences>> {
        createDataStore()
    }
}