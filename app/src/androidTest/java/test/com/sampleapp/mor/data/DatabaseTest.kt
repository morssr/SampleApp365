package test.com.sampleapp.mor.data

//import org.hamcrest.Matchers.*

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import test.com.sampleapp.mor.data.api.FakeApiDataFactory
import test.com.sampleapp.mor.data.cache.AppDatabase
import test.com.sampleapp.mor.data.cache.PropertiesDao
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.data.cache.utilities.converters.QueryHelper
import test.com.sampleapp.mor.di.DatabaseModule
import test.com.sampleapp.mor.ui.properties.PropertyStatusFilter

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val db: AppDatabase = DatabaseModule.provideAppDatabase(context, true)
    private val propertiesDao: PropertiesDao = db.propertiesDao()

    private val firstPropertiesList = FakeApiDataFactory.getFirstPagePropertiesAndTenants(context)

    @Before
    fun setUp() {
        runBlocking {
            populateDbWithFirstPage()
        }
    }

    @After
    fun cleanDb() {
        runBlocking {
            db.propertiesDao().clearAll()
            db.tenantsDao().clearAll()
        }
    }

    @Test
    fun queryAllPropertiesAndTenants_IsNotEmpty() {
        runBlocking {

            val propertiesAndTenants = propertiesDao.getAllProperties()
            printQuery(propertiesAndTenants)

            ViewMatchers.assertThat(
                propertiesAndTenants, `is`(not(empty()))
            )
        }
    }

    @Test
    fun queryPropertiesByInactiveStatus_validateTheResult() {
        runBlocking {

            val propertyStatus = PropertyStatusFilter.INACTIVE
            val queryString = QueryHelper.buildPropertiesByStatusQuery(propertyStatus)
            val inactiveProperties =
                propertiesDao.getPropertiesRawQuery(SimpleSQLiteQuery(queryString))

            inactiveProperties.forEach {
                assertThat(it.property.occupationStatus, `is`(equalTo(OccupationStatus.INACTIVE)))
            }
        }
    }

    @Test
    fun queryPropertiesByOccupiedStatus_validateTheResult() {
        runBlocking {

            val propertyStatus = PropertyStatusFilter.OCCUPIED
            val queryString = QueryHelper.buildPropertiesByStatusQuery(propertyStatus)
            val inactiveProperties =
                propertiesDao.getPropertiesRawQuery(SimpleSQLiteQuery(queryString))

            inactiveProperties.forEach {
                assertThat(it.property.occupationStatus, `is`(equalTo(OccupationStatus.OCCUPIED)))
            }
        }
    }

    private suspend fun populateDbWithFirstPage() {
        propertiesDao.insertAllPropertiesAndTenants(firstPropertiesList)
    }


    private fun printQuery(list: List<PropertyAndTenant>) {
        println("printQuery: list size:${list.size}")
        list.forEach {
            println(it)
        }
    }
}