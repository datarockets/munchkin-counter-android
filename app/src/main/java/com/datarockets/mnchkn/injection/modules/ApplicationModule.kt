package com.datarockets.mnchkn.injection.modules

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.datarockets.mnchkn.BuildConfig
import com.datarockets.mnchkn.injection.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun providesContext(): Context {
        return application
    }

    @Provides
    fun providesCrashlyticsCore(): CrashlyticsCore {
        return CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()
    }

    @Provides
    fun providesCrashlytics(crashlyticsCore: CrashlyticsCore): Crashlytics {
        return Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()
    }
}
