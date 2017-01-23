package com.datarockets.mnchkn

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics

import com.datarockets.mnchkn.injection.components.ApplicationComponent
import com.datarockets.mnchkn.injection.components.DaggerApplicationComponent
import com.datarockets.mnchkn.injection.modules.ApplicationModule
import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.fabric.sdk.android.Fabric

import timber.log.Timber
import javax.inject.Inject

class MunchkinApplication : Application() {

    lateinit var mApplicationComponent: ApplicationComponent
    private var mMixpanel: MixpanelAPI? = null

    @Inject lateinit var mCrashlytics: Crashlytics

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        mApplicationComponent.inject(this)

        Fabric.with(this, mCrashlytics)

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
