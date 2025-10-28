package dev.gaddal.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.gaddal.core.data.auth.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module that provides the Android-specific implementation of [HttpClientEngine].
 *
 * This module configures [OkHttp] as the HTTP client engine for the Android platform
 * and sets up Android-specific data storage through [DataStore].
 */
actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore(androidContext())
    }
}