package com.datarockets.mnchkn.injection.modules

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager

import com.datarockets.mnchkn.injection.ActivityContext

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun providesActivity(): Activity {
        return activity
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return activity
    }

    @Provides
    fun providesSupportFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun providesLinearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }
}
