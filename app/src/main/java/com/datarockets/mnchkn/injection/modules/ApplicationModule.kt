package com.datarockets.mnchkn.injection.modules

import android.app.Application
import android.content.Context

import com.datarockets.mnchkn.injection.ApplicationContext

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

}
