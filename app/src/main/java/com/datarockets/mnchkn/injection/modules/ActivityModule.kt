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
class ActivityModule(private val mActivity: AppCompatActivity) {

    @Provides
    fun providesActivity(): Activity {
        return mActivity
    }

    @Provides
    @ActivityContext
    fun providesContext(): Context {
        return mActivity
    }

    @Provides
    fun providesSupportFragmentManager(): FragmentManager {
        return mActivity.supportFragmentManager
    }

    @Provides
    fun providesLinearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(mActivity)
    }

}
