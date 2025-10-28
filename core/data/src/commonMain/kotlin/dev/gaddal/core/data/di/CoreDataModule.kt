package dev.gaddal.core.data.di

import dev.gaddal.core.data.auth.DataStoreSessionStorage
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.data.auth.KtorAuthService
import dev.gaddal.core.data.logging.KermitLogger
import dev.gaddal.core.data.networking.HttpClientFactory
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.logging.ChirpLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Platform-specific Koin module for providing platform-dependent dependencies.
 *
 * This `expect` declaration allows each platform (e.g., Android, iOS) to provide its own
 * implementation of the module, which is then included in the common [coreDataModule].
 */
expect val platformCoreDataModule: Module

/**
 * Core Koin module for data-related dependencies.
 *
 * This module provides essential services for the data layer, including:
 * - A platform-specific `HttpClientEngine` via [platformCoreDataModule].
 * - A [ChirpLogger] implementation using [KermitLogger].
 * - An `HttpClient` instance created by [HttpClientFactory].
 * - An [AuthService] implementation using [KtorAuthService].
 * - A [SessionStorage] implementation using [DataStoreSessionStorage].
 *
 * It serves as a central point for defining dependencies required by the data components of the application.
 */
val coreDataModule = module {
    includes(platformCoreDataModule)
    single<ChirpLogger> { KermitLogger }
    single {
        HttpClientFactory(get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}