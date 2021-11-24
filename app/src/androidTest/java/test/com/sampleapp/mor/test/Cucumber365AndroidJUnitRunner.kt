package test.com.sampleapp.mor.test

import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.CucumberOptions

@CucumberOptions(features = ["features"], glue = ["test.com.sampleapp.mor.cucumber.steps"])
class Cucumber365AndroidJUnitRunner : CucumberAndroidJUnitRunner()