package dev.gaddal.chirp.di

import dev.gaddal.auth.presentation.di.authPresentationModule
import dev.gaddal.chat.presentation.di.chatPresentationModule
import dev.gaddal.core.data.di.coreDataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes the Koin dependency injection framework and loads the necessary modules.
 *
 * @param config An optional lambda to configure the Koin application instance. Can be used to add
 * Android-specific configurations like context or logging.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            authPresentationModule,
            appModule,
            chatPresentationModule
        )
    }
}