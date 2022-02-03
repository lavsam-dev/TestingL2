package lavsam.gb.testingl2.recycler

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.delay
import lavsam.gb.testingl2.R
import lavsam.gb.testingl2.view.MainActivity
import lavsam.gb.testingl2.view.SearchResultAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_ScrollTo() {
        loadList()

        Espresso.onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("cloudflare/cobweb"))
                )
            )
    }

    @Test
    fun activitySearch_PerformClickAtPosition() {
        loadList()

        Espresso.onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                20, ViewActions.click()
            )
        )
    }

    @Test
    fun activitySearch_PerformClickOnItem() {
        loadList()

        Espresso.onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("bmTas/JRecord"))
                )
            )

        Espresso.onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("ajlopez/CobolScript")),
                    ViewActions.click()
                )
            )
    }

    @Test
    fun activitySearch_PerformCustomClick() {
        loadList()

        Espresso.onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                        0,
                        tapOnItemWithId(R.id.checkbox)
                    )
            )
    }

    @After
    fun close() {
        scenario.close()
    }

    private fun loadList() {
        Espresso.onView(withId(R.id.searchEditText)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.searchEditText))
            .perform(ViewActions.replaceText("COBOL"), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.searchEditText)).perform(ViewActions.pressImeActionButton())
        Espresso.onView(ViewMatchers.isRoot()).perform(delay())
    }

    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Tap on view with defined id"
        }

        override fun perform(uiController: UiController?, view: View?) {
            val keySearch = view?.findViewById(id) as View
            keySearch.performClick()
        }
    }

    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $3 seconds"

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(3000)
            }
        }
    }
}