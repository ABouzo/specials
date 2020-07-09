package com.bouzo.specials

import android.content.res.Resources
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.bouzo.specials.restapi.DealsService
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class DealListFragmentTest : KoinTest {

    @get:Rule
    var rule = ActivityTestRule(MainActivity::class.java, true, false)
    val mockWebServer: MockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)

        loadKoinModules(
            module {
                factory(override = true) {
                    Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://localhost:8080/")
                        .build().create(DealsService::class.java) as DealsService
                }
            }
        )
    }

    @After
    fun cleanUp() {
        mockWebServer.close()
    }


    @Test
    fun recyclerCorrectChildCount() {
        mockWebServer.enqueue(
            MockResponse().setBody(RESPONSE)
        )
        rule.launchActivity(null)
        onView(withId(R.id.deal_list_recycler_view)).check(matches(hasChildCount(3)))
    }

    @Test
    fun sizeCalculatedCorrectly() {
        mockWebServer.enqueue(MockResponse().setBody(RESPONSE))
        rule.launchActivity(null)

        val unitInPixels = Resources.getSystem().displayMetrics.widthPixels / 16

        onView(allOf(withId(R.id.deal_container), withChild(withChild(withText("Noodle Dish with Roasted Black Bean Sauce"))))).check { view, noViewFoundException ->
            assertEquals(unitInPixels * 16, view.layoutParams.width + view.marginRight + view.marginLeft)
            assertEquals(unitInPixels * 8, view.layoutParams.height + view.marginTop + view.marginBottom)
        }
    }

    companion object {
        const val RESPONSE =
            "           {  \n" +
                    "               \"canvasUnit\":16,\n" +
                    "               \"managerSpecials\":[  \n" +
                    "                  {  \n" +
                    "                     \"imageUrl\":\"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/L.png\",\n" +
                    "                     \"width\":16,\n" +
                    "                     \"height\":8,\n" +
                    "                     \"display_name\":\"Noodle Dish with Roasted Black Bean Sauce\",\n" +
                    "                     \"original_price\":\"2.00\",\n" +
                    "                     \"price\":\"1.00\"\n" +
                    "                  },\n" +
                    "                  {  \n" +
                    "                     \"imageUrl\":\"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/J.png\",\n" +
                    "                     \"width\":8,\n" +
                    "                     \"height\":8,\n" +
                    "                     \"display_name\":\"Onion Flavored Rings\",\n" +
                    "                     \"original_price\":\"2.00\",\n" +
                    "                     \"price\":\"1.00\"\n" +
                    "                  },\n" +
                    "                  {  \n" +
                    "                     \"imageUrl\":\"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/K.png\",\n" +
                    "                     \"width\":8,\n" +
                    "                     \"height\":8,\n" +
                    "                     \"display_name\":\"Kikkoman Less Sodium Soy Sauce\",\n" +
                    "                     \"original_price\":\"2.00\",\n" +
                    "                     \"price\":\"1.00\"\n" +
                    "                  }\n" +
                    "               ]\n" +
                    "            }"
    }
}