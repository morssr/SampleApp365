package test.com.sampleapp.mor

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith
import test.com.sampleapp.mor.data.api.FakeApiDataFactory
import test.com.sampleapp.mor.data.api.FakeApiDataFactory.propertiesPage1FileName
import test.com.sampleapp.mor.data.api.FakeApiDataFactory.propertiesPage2FileName

@RunWith(AndroidJUnit4::class)
class FakeApiDataFactoryTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun assertThat_propertiesJsonFile_isExistInAssetsDirectory() {
        listOf(
            FakeApiDataFactory.getPropertiesJsonFile(context, propertiesPage1FileName),
            FakeApiDataFactory.getPropertiesJsonFile(context, propertiesPage2FileName)
        ).forEach {
            assertThat(it, `is`(notNullValue()))
        }
    }

    @Test
    fun getPropertiesList_CheckIfListContainsAtLeastTwoElements() {
        val propertiesListSize =
            FakeApiDataFactory.getPropertiesList(context, FakeApiDataFactory.propertiesJsonFileNameMap, 2)
        assertThat(propertiesListSize, `is`(not(empty())))
    }
}