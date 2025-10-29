package dev.gaddal.chirp

import android.app.Application
import dev.gaddal.chirp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/**
 * The main Application class for the Chirp application.
 *
 * This class is responsible for initializing global configurations and libraries when the
 * application starts. It extends the Android `Application` class and serves as the entry
 * point for application-wide setup.
 *
 * Specifically, it initializes Koin, the dependency injection framework, with the Android
 * application context and logger configurations.
 */
class ChirpApplication: Application() {

    /**
     * Called when the application is starting, before any other application objects have been created.
     * This method is used to perform app-wide initialization tasks.
     *
     * In this implementation, the Koin dependency injection framework is initialized with
     * configurations specific to the Android platform, such as setting the application context
     * and enabling logging for dependency injection operations.
     */
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ChirpApplication)
            androidLogger()
        }
    }
}