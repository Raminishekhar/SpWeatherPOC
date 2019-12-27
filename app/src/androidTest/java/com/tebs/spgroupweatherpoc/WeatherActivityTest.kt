package com.tebs.spgroupweatherpoc

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.tebs.spgroupweatherpoc.Activities.WeatherActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class WeatherActivityTest {

    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(WeatherActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun shouldInflateLayout() {
        val tested = Mockito.spy(WeatherActivity())
        tested.onCreate(null)
        Mockito.verify(tested).setContentView(R.layout.activity_weather)
    }

    @Test
    fun setWeatherDescrption() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_humidity)).perform(
            ViewActions.typeText(
                "Weather Desc : sunny"
            )
        )
        Espresso.onView(ViewMatchers.withText("Weather Desc : sunny"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun setTemparature() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_temp)).perform(
            ViewActions.typeText(
                "Temperature : 30C"
            )
        )
        Espresso.onView(ViewMatchers.withText("Temperature : 30C"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun setWeatherDescription() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_weather_desc)).perform(
            ViewActions.typeText(
                "Weather Desc : sunny"
            )
        )
        Espresso.onView(ViewMatchers.withText("Weather Desc : sunny"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}