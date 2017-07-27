package com.datarockets.mnchkn.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.datarockets.mnchkn.injection.ApplicationContext

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper
@Inject constructor(@ApplicationContext context: Context) {

    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun checkIsUserSeenOnboarding(): Boolean {
        return pref.getBoolean(IS_ONBOARDING_SEEN, false)
    }

    fun setOnboardingSeen() {
        pref.edit().putBoolean(IS_ONBOARDING_SEEN, true).apply()
    }

    val isWakeLockActive: Boolean
        get() = pref.getBoolean(IS_WAKELOCK_ACTIVE, false)

    fun setWakeLock(isActive: Boolean) {
        pref.edit().putBoolean(IS_WAKELOCK_ACTIVE, isActive).apply()
    }

    val isGameStarted: Boolean
        get() = pref.getBoolean(IS_GAME_STARTED, false)

    fun setGameStatus(isStarted: Boolean) {
        pref.edit().putBoolean(IS_GAME_STARTED, isStarted).apply()
    }

    val isFirstLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_LAUNCH, true)

    fun setFirstLaunch(isFirstLaunch: Boolean) {
        pref.edit().putBoolean(IS_FIRST_LAUNCH, isFirstLaunch).apply()
    }

    companion object {
        private val IS_ONBOARDING_SEEN = "is_onboarding_seen"
        private val IS_WAKELOCK_ACTIVE = "is_wakelock_active"
        private val IS_GAME_STARTED = "is_game_started"
        val IS_FIRST_LAUNCH = "is_first_launch"
    }
}
