package test.com.sampleapp.mor.cucumber.steps

import io.cucumber.java.en.Given
import test.com.sampleapp.mor.ActivityScenarioHolder
import test.com.sampleapp.mor.MainActivityScreenRobot

class MainActivitySteps {

    private val robot = MainActivityScreenRobot()
    private val scenario = ActivityScenarioHolder()

    @Given("I have main a activity")
    fun i_have_a_MainActivity() {
        robot.launchMainActivityScreen(scenario)
    }

}