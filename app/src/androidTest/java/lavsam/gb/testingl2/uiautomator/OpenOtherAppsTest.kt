package lavsam.gb.testingl2.uiautomator

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class OpenOtherAppsTest {
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun test_OpenSettings() {
        uiDevice.pressHome()
//        Open a screen with a list of installed applications.
//        Please note that on the device for which this test was written (Android_Emulator - Pixel_2_Q_10_api),
//        the list of applications opens with a swipe from the bottom up on the main screen.
//        The swipe method takes the coordinates of the start and end points of the swipe.
//        In our case, this is approximately from the bottom of the screen straight up. Steps indicates in
//        how many steps we want to swipe: the higher the number,
//        the slower the swipe will be
        uiDevice.swipe(500, 1500, 500, 0, 5)
//        val appsTab: UiObject = uiDevice.findObject(UiSelector().text("Системные приложения"))
//        appsTab.click()
//        There are usually so many applications installed that the button may be off the screen
//        Then the root container will be Scrollable.
//        If all applications fit on one screen, then it is enough to set scrollable (false)
        val appViews = UiScrollable(UiSelector().scrollable(false))
//        We find in the container the settings by the name of the icon
        val settingsApp =
            appViews.getChildByText(UiSelector().className(TextView::class.java.name), "Settings")
        settingsApp.clickAndWaitForNewWindow()
//        Make sure Settings is open
        val settingsValidation = uiDevice.findObject(UiSelector().packageName("com.android.settings"))
        Assert.assertTrue(settingsValidation.exists())
    }
}