package dev.gaddal.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/**
 * Koin module that provides the iOS-specific implementation of [HttpClientEngine].
 *
 * This module configures [Darwin] as the HTTP client engine for the iOS platform.
 */

actual val platformCoreDataModule = module {
    single<HttpClientEngine> { Darwin.create() }
}