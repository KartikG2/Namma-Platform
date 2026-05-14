package com.namma.platform

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for NammaPlatform.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation
 * and serve as the application-level dependency container.
 */
@HiltAndroidApp
class NammaPlatformApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        com.namma.platform.util.NetworkUtil.initialize(this)
    }
}
