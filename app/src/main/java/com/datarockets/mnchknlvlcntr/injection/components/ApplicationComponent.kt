package com.datarockets.mnchknlvlcntr.injection.components

import android.app.Application
import android.content.Context
import com.datarockets.mnchknlvlcntr.MunchkinApplication
import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.data.local.DatabaseHelper
import com.datarockets.mnchknlvlcntr.data.local.PreferencesHelper
import com.datarockets.mnchknlvlcntr.injection.ApplicationContext
import com.datarockets.mnchknlvlcntr.injection.modules.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(munchkinApplication: MunchkinApplication)

    @ApplicationContext fun context(): Context
    fun application(): Application
    fun preferencesHelper(): PreferencesHelper
    fun databaseHelper(): DatabaseHelper
    fun dataManager(): DataManager
}
