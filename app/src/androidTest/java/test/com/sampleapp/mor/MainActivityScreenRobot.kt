package test.com.sampleapp.mor

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry

class MainActivityScreenRobot {

    fun launchMainActivityScreen(scenarioHolder: ActivityScenarioHolder) =
        scenarioHolder.launch(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                MainActivity::class.java
            )
        )
}