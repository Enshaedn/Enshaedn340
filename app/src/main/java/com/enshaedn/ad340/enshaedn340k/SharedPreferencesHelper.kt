package com.enshaedn.ad340.enshaedn340k

import android.content.SharedPreferences


class SharedPreferencesHelper(sharedPrefs: SharedPreferences) {
    private var mSharedPrefs = sharedPrefs
    private val KEY_ENTRY: String = "textEntry"

    fun saveEntry(str: String): Boolean {
        val editor: SharedPreferences.Editor = mSharedPrefs.edit()
        editor.putString(KEY_ENTRY, str)
        return editor.commit()
    }

    fun getEntry(): String {
        val str: String = mSharedPrefs.getString(KEY_ENTRY, "")
        return str
    }
}