package com.datarockets.mnchkn.injection.modules

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.datarockets.mnchkn.BuildConfig

import com.datarockets.mnchkn.injection.ApplicationContext
import com.mixpanel.android.mpmetrics.MixpanelAPI

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(protected val mApplication: Application) {

    @Provides
    fun providesApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    fun providesContext(): Context {
        return mApplication
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
