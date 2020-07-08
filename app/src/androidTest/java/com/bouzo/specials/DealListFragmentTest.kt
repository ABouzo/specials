package com.bouzo.specials

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DealListFragmentTest {

    @get:Rule
    var activity = activityScenarioRule<MainActivity>()

    @Test
    fun fragment_display_correct_title() {
        onView(withId(R.id.deal_list_title)).check(matches(withText(R.string.deal_list_page_title)))
    }
}