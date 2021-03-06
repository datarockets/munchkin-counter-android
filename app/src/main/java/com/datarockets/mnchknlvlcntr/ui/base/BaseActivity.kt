package com.datarockets.mnchknlvlcntr.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate

import com.datarockets.mnchknlvlcntr.MunchkinApplication
import com.datarockets.mnchknlvlcntr.injection.components.ActivityComponent
import com.datarockets.mnchknlvlcntr.injection.components.DaggerActivityComponent
import com.datarockets.mnchknlvlcntr.injection.modules.ActivityModule
import com.mixpanel.android.mpmetrics.MixpanelAPI

import org.json.JSONException
import org.json.JSONObject

import timber.log.Timber

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private var activityComponent: ActivityComponent? = null
    private var mixpanel: MixpanelAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        mixpanel = MunchkinApplication[this].mixpanel
    }

    fun trackWithProperties(title: String, propertyName: String, propertyData: String) {
        val props = JSONObject()
        try {
            props.put(propertyName, propertyData)
        } catch (e: JSONException) {
            Timber.e("Error while trying to send tracked event")
        }

        mixpanel!!.track(title, props)
    }

    fun trackWithoutProperties(eventName: String) {
        mixpanel!!.track(eventName)
    }

    fun activityComponent(): ActivityComponent {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(MunchkinApplication[this].applicationComponent)
                    .activityModule(ActivityModule(this))
                    .build()
        }
        return activityComponent!!
    }
}
