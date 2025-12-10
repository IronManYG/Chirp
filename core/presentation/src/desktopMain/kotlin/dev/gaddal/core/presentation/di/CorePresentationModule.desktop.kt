package dev.gaddal.core.presentation.di

import dev.gaddal.core.presentation.util.LocaleApplier
import org.koin.dsl.module

actual val platformCorePresentationModule = module {
    single { LocaleApplier() }
}