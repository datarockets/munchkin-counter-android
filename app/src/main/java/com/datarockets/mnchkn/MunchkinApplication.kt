package com.datarockets.mnchkn

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.datarockets.mnchkn.data.local.MunchkinDatabase

import com.datarockets.mnchkn.injection.components.ApplicationComponent
import com.datarockets.mnchkn.injection.components.DaggerApplicationComponent
import com.datarockets.mnchkn.injection.modules.ApplicationModule
import com.mixpanel.android.mpmetrics.MixpanelAPI
import io.fabric.sdk.android.Fabric

import timber.log.Timber
import javax.inject.Inject

class MunchkinApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    private var mixpanelApi: MixpanelAPI? = null

    @Inject lateinit var crashlytics: Crashlytics

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent.inject(this)

        Fabric.with(this, crashlytics)

        database = Room.databaseBuilder(this, MunchkinDatabase::class.java, "players_db").build()
    }

    val mixpanel: MixpanelAPI
        @Synchronized get() {
            if (mixpanelApi == null) {
                mixpanelApi = MixpanelAPI.getInstance(this, BuildConfig.MIXPANEL_API_KEY)
            }
            return mixpanelApi!!
        }

    companion object {
        operator fun get(context: Context): MunchkinApplication {
            return context.applicationContext as MunchkinApplication
        }

        lateinit var database: MunchkinDatabase
    }
}
