package dev.gaddal.core.presentation.di

import dev.gaddal.core.presentation.util.LocaleApplier
import org.koin.dsl.module

/**
 * Platform-specific Koin module declaration for core presentation dependencies.
 *
 * This module provides definitions for platform-specific components required
 * for the presentation layer. It includes utilities such as `LocaleApplier`,
 * which enables regional settings like language adjustments on the iOS platform.
 */
actual val platformCorePresentationModule = module {
    single { LocaleApplier() }
}