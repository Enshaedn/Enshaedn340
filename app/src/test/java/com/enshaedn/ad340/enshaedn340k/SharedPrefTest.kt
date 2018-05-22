package com.enshaedn.ad340.enshaedn340k

import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.mockito.ArgumentMatchers.*
import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class SharedPrefTest {

    @Mock
    lateinit var mSharedPreferences: SharedPreferences

    @Mock
    lateinit var mEditor: SharedPreferences.Editor

    lateinit private var mSharedPrefsHelper: SharedPreferencesHelper

    private val textEntry: String = "This is a test"

    @Before
    fun initMock() {
        //Create mocked SharedPreferences
        mSharedPrefsHelper = createSharedPrefHelper()
    }

    @Test
    fun testSharedPrefs() {
        //Save the personal information to SharedPreferences
        val success: Boolean = mSharedPrefsHelper.saveEntry(textEntry)

        assertThat("SharedPreferencesEntry.save... returns true", success)

        assertEquals(textEntry, mSharedPrefsHelper.getEntry())
    }

    private fun createSharedPrefHelper(): SharedPreferencesHelper {
        //Mock reading the SharedPreferences as if mSharedPreferences was previously written correctly
        `when`(mSharedPreferences.getString(eq("textEntry"), anyString())).thenReturn(textEntry)
        //Mocking a successful commit
        `when`(mEditor.commit()).thenReturn(true)
        //Return the MockEditor when requested
        `when`(mSharedPreferences.edit()).thenReturn(mEditor)

        return SharedPreferencesHelper(mSharedPreferences)
    }
}