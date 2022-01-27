package lavsam.gb.testingl2

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert
import lavsam.gb.testingl2.view.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {
    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            Assert.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        org.junit.Assert.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityEditText_NotNull() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            editText.setText("text")
            editText.setText("text", TextView.BufferType.EDITABLE)
            Assert.assertNotNull(editText.text)
            org.junit.Assert.assertEquals("text", editText.text.toString())
        }
    }

    @Test
    fun activityEditText_isVisible() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            org.junit.Assert.assertEquals(View.VISIBLE, editText.visibility)
        }
    }

    @Test
    fun mainActivityButtonToDetailsActivity_isVisible() {
        scenario.onActivity {
            val buttonToDetailsActivity = it.findViewById<Button>(R.id.toDetailsActivityButton)
            org.junit.Assert.assertEquals(View.VISIBLE, buttonToDetailsActivity.visibility)
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}