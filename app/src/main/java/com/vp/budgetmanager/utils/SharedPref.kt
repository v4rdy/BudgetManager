package com.vp.budgetmanager.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPref private constructor() {

    companion object {
        private val sharePref = SharedPref()
        private lateinit var sharedPreferences: SharedPreferences

        fun getInstance(context: Context): SharedPref {
            if (!::sharedPreferences.isInitialized) {
                synchronized(SharedPref::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences =
                            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }
    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0.0F)
    }

    fun saveString(data: String, key: String) {
        sharedPreferences.edit()
            .putString(key, data)
            .apply()
    }

    fun saveFloat(data: Float, key: String) {
        sharedPreferences.edit()
            .putFloat(key, data)
            .apply()
    }

    fun removeString(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
