package com.luismunyoz.dirbrowser

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.luismunyoz.dirbrowser.app.browser.BrowserActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BrowserActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var browserActivityTestRule = ActivityScenarioRule(BrowserActivity::class.java)

    @Before
    fun beforeTest() {
        hiltRule.inject()
    }

    @Test
    fun testTitle() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

}