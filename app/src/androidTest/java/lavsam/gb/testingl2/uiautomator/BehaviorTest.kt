package lavsam.gb.testingl2.uiautomator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setup() {
//        First, we turn off all applications if we have something running
        uiDevice.pressHome()

//        Launching our application
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
//        We already checked the Intent for null in the previous test, so we assume that our Intent is not null
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

//        We are waiting for the application to open on the smartphone to start testing its elements
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

//    Make sure the application is open. To do this, it is enough to find any element on the screen
//    and check it for null
    @Test
    fun test_MainActivityIsStarted() {
//        Through uiDevice we find editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        Assert.assertNotNull(editText)
    }

//    Making sure the search is working as expected
    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
//        Setting the value
        editText.text = "UiAutomator"
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

//        We are waiting for a specific event: the appearance of the totalCountTextView text field.
//        This will mean that the server returned a response with some data, that is, the request completed.
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
//        We make sure that the server returned the correct result. Please note that the quantity
//        results may vary over time because the number of repositories is constantly changing.
        Assert.assertEquals(changedText.text.toString(), "Number of results: 696")
    }

    @Test
    fun test_ButtonsMainActivity_NotNull() {
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        Assert.assertNotNull(searchButton)
        Assert.assertNotNull(toDetails)
    }

    @Test
    fun test_OpenDetailsScreen() {
//        Finding a button
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
//        We click on the button
        toDetails.click()

//        We are waiting for a specific event: the appearance of the totalCountTextView text field.
//        This will mean that DetailsScreen has opened and this field is visible on the screen.
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)

//        We make sure that the field is visible and contains the intended text.
//        Note that the text should be "Number of results: 0"
//        since we are clicking on the button without sending any search queries.
//        To check if a certain number of repositories are displayed,
//        you in the same method need to send a request to the server and open the DetailsScreen.
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    @Test
    fun test_OpenDetailsScreenWithData() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "UiAutomator"
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedText.text, "Number of results: 696")
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 696")
    }

    @Test
    fun test_ButtonIsIncrementing() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 0")

        val toIncrementingButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        toIncrementingButton.click()
        val changedTextView = uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedTextView.text.toString(), "Number of results: 1")
    }

    @Test
    fun test_ButtonIsDecrementing() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 0")

        val toDecrementingButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        toDecrementingButton.click()
        val changedTextView = uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedTextView.text.toString(), "Number of results: -1")
    }

    @Test
    fun test_ButtonsDetailsActivity_NotNull() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        val toDecrementingButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        val toIncrementingButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        Assert.assertNotNull(toIncrementingButton)
        Assert.assertNotNull(toDecrementingButton)
        Assert.assertNotNull(detailsActivityText)
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}