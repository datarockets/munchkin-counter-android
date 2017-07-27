package com.datarockets.mnchkn.ui.settings

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.local.PreferencesHelper

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        findPreference(getString(R.string.settings_show_tutorial_key)).onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if (preference?.key.equals(getString(R.string.settings_show_tutorial_key))) {
            PreferenceManager.getDefaultSharedPreferences(preference?.context).edit()
                    .putBoolean(PreferencesHelper.IS_FIRST_LAUNCH, true).apply()
            
            val intent = preference?.context?.packageManager?.getLaunchIntentForPackage(preference.context?.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            preference?.context?.startActivity(intent)
            return true
        } else {
            return false
        }
    }
}
