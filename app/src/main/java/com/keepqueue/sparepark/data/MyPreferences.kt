package com.keepqueue.sparepark.data

import android.app.Activity
import android.content.Context

object MyPreferences {

    private const val PREF_FILE = "my_prefs"
    private const val IS_LOGGED_IN = "isLoggedIn"
    private const val USER_ID = "userId"
    private const val USER_NAME = "userName"

    fun setLoggedIn(activity: Activity,
                    loginStatus: Boolean,
                    userId: Int,
                    userName: String) {
        val sharedPref = activity.getSharedPreferences(
            PREF_FILE, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(IS_LOGGED_IN, loginStatus)
            putInt(USER_ID, userId)
            putString(USER_NAME, userName)
            apply()
        }
    }

    fun isLoggedIn(activity: Activity): Boolean {
        val sharedPref = activity.getSharedPreferences(
            PREF_FILE, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUserId(activity: Activity): Int {
        val sharedPref = activity.getSharedPreferences(
            PREF_FILE, Context.MODE_PRIVATE)
        return sharedPref.getInt(USER_ID, -1)
    }


}