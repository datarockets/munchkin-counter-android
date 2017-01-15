package com.datarockets.mnchkn.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.datarockets.mnchkn.MunchkinApplication
import com.datarockets.mnchkn.injection.components.ActivityComponent
import com.datarockets.mnchkn.injection.components.DaggerActivityComponent
import com.datarockets.mnchkn.injection.modules.ActivityModule
import com.mixpanel.android.mpmetrics.MixpanelAPI

import org.json.JSONException
import org.json.JSONObject

import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

    private var mActivityComponent: ActivityComponent? = null
    private var mMixpanel: MixpanelAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMixpanel = MunchkinApplication[this].mixpanel
    }

    fun trackWithProperties(title: String, propertyName: String, propertyData: String) {
        val props = JSONObject()
        try {
            props.put(propertyName, propertyData)
        } catch (e: JSONException) {
            Timber.e("Error while trying to send tracked event")
        }

        mMixpanel!!.track(title, props)
    }

    fun trackWithoutProperties(eventName: String) {
        mMixpanel!!.track(eventName)
    }

    fun activityComponent(): ActivityComponent {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(MunchkinApplication[this].component)
                    .activityModule(ActivityModule(this))
                    .build()
        }
        return mActivityComponent!!
    }

}
