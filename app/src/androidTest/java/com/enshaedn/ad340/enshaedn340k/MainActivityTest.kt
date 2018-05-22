package com.enshaedn.ad340.enshaedn340k

import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule @JvmField
    val tActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)
    val tPhrase: String = "Test Phrase"
    val tToastMsg: String = "You must enter something"

    @Before
    fun setUp() {
    }

    @Test
    fun uiExists() {
        onView(withId(R.id.editText)).check(matches(isDisplayed()))
        onView(withId(R.id.button)).check(matches(isDisplayed()))
    }

    @Test
    fun testPassPhrase() {
        //clear text then input some text
        onView(withId(R.id.editText)).perform(clearText(), typeText(tPhrase))
        //close soft keyboard
        closeSoftKeyboard()
        //click button
        onView(withId(R.id.button)).perform(click())
        //check text
        onView(withId(R.id.textView3)).check(matches(withText(tPhrase)))
    }

    @Test
    fun testPhraseFail() {
        //clear text
        onView(withId(R.id.editText)).perform(clearText())
        //try to click without text
        onView(withId(R.id.button)).perform(click())
        //verify toast displayed requesting text
        onView(withText(tToastMsg)).inRoot(withDecorView(not(tActivityTestRule.activity.window.decorView))).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
    }
}