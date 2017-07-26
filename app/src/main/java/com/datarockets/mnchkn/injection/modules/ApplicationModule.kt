package com.datarockets.mnchkn.injection.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.datarockets.mnchkn.BuildConfig
import com.datarockets.mnchkn.MunchkinApplication
import com.datarockets.mnchkn.data.local.GameDao
import com.datarockets.mnchkn.data.local.MunchkinDatabase
import com.datarockets.mnchkn.data.local.PlayerDao
import com.datarockets.mnchkn.injection.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun providesApplicationContext(): Context {
        return application
    }

    @Provides
    fun providesContext(): Context {
        return application.applicationContext
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

    @Singleton
    @Provides
    fun providesDatabase(context: Context): MunchkinDatabase {
        return Room.databaseBuilder(context, MunchkinDatabase::class.java, "players_db").build()
    }

    @Singleton
    @Provides
    fun providePlayerDao(database: MunchkinDatabase): PlayerDao {
        return database.playerDao()
    }

    @Singleton
    @Provides
    fun provideGameDao(database: MunchkinDatabase): GameDao {
        return database.gameDao()
    }
}
