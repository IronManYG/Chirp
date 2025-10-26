package dev.gaddal.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

/**
 * Koin module that provides the Android-specific implementation of [HttpClientEngine].
 *
 * This module configures [OkHttp] as the HTTP client engine for the Android platform.
 */

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}