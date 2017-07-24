package com.datarockets.mnchkn.ui.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.datarockets.mnchkn.R

class SettingsFragment: PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
