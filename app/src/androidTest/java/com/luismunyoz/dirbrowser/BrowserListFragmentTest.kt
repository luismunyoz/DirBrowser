package com.luismunyoz.dirbrowser

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.luismunyoz.dirbrowser.app.browser.list.BrowserListFragment
import com.luismunyoz.dirbrowser.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BrowserListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun beforeTest() {
        hiltRule.inject()
    }

    @Test
    fun testList() {
        launchFragmentInHiltContainer<BrowserListFragment>(
            fragmentArgs = bundleOf(
                "name" to "The name",
                "folder_id" to "id"
            )
        )
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }
}