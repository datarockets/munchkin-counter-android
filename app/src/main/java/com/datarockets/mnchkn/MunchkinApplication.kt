package com.datarockets.mnchkn

import android.app.Application
import android.content.Context

import com.datarockets.mnchkn.injection.components.ApplicationComponent
import com.datarockets.mnchkn.injection.components.DaggerApplicationComponent
import com.datarockets.mnchkn.injection.modules.ApplicationModule
import com.mixpanel.android.mpmetrics.MixpanelAPI

import timber.log.Timber

class MunchkinApplication : Application() {

    lateinit var component: ApplicationComponent
    var mMixpanel: MixpanelAPI? = null

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component.inject(this)
    }

    val mixpanel: MixpanelAPI
        @Synchronized get() {
            if (mMixpanel == null) {
                mMixpanel = MixpanelAPI.getInstance(this, BuildConfig.MIXPANEL_API_KEY)
            }
            return mMixpanel!!
        }

    companion object {

        operator fun get(context: Context): MunchkinApplication {
            return context.applicationContext as MunchkinApplication
        }
    }

}
