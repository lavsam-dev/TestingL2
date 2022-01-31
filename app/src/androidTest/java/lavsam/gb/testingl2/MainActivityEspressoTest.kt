package lavsam.gb.testingl2

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import lavsam.gb.testingl2.view.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityButton_IsEffectiveVisible() {
        Espresso.onView(withId(R.id.toDetailsActivityButton))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun activityButton_IsCompletelyDisplayed() {
        Espresso.onView(withId(R.id.toDetailsActivityButton))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
    }

    @Test
    fun activityTextView_isInvisible() {
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    @Test
    fun activityTextView_HasText() {
        Espresso.onView(withId(R.id.toDetailsActivityButton))
            .check(ViewAssertions.matches(ViewMatchers.withText("to details")))
    }

    @Test
    fun activityEditText_IsDisplayed() {
        Espresso.onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun activityEditText_IsCompletelyDisplayed() {
        Espresso.onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
    }

    @Test
    fun activityEditText_IsHintDisplayed() {
        Espresso.onView(withId(R.id.searchEditText))
            .check(ViewAssertions.matches(ViewMatchers.withHint("Enter keyword e.g. android")))
    }

    @Test
    fun activitySearch_IsWorking() {
        Espresso.onView(withId(R.id.searchEditText)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.searchEditText))
            .perform(ViewActions.replaceText("algol"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.searchEditText)).perform(ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.isRoot()).perform(delay())
        Espresso.onView(withId(R.id.totalCountTextView))
            .check(ViewAssertions.matches(ViewMatchers.withText("Number of results: 2806")))
    }

    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $4 seconds"

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(3000)
            }
        }
    }
}