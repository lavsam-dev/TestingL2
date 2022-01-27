package lavsam.gb.testingl2.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class InitialClass {
//    We need the context to launch the necessary screens and get the packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

//    The path to the classes of our application that we will be testing
    private val packageName = context.packageName

    @Test
    fun test_DeviceNotNull() {
//        The UiDevice class provides access to your device.
//        It is through UiDevice that you can control the device, open applications
//        and find the necessary elements on the screen
        val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Assert.assertNotNull(uiDevice)
    }

//    Check if the application exists
    @Test
    fun test_AppPackageNotNull() {
        Assert.assertNotNull(packageName)
    }

//    We check that the Intent for launching our application is not null
    @Test
    fun test_MainActivityIntentNotNull() {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        Assert.assertNotNull(intent)
    }
}