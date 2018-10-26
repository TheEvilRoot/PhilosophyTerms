package com.theevilroot.philosophy

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.net.URL

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.theevilroot.philosophy", appContext.packageName)
    }
}
