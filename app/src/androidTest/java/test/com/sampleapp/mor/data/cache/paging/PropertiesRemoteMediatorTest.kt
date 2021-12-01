package test.com.sampleapp.mor.data.cache.paging

import androidx.paging.*
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import test.com.sampleapp.mor.data.api.FakeApiDataFactory
import test.com.sampleapp.mor.data.api.PropertiesService
import test.com.sampleapp.mor.data.cache.AppDatabase
import test.com.sampleapp.mor.data.cache.PropertiesDao
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.di.DatabaseModule

@ExperimentalPagingApi
class PropertiesRemoteMediatorTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val db: AppDatabase = DatabaseModule.provideAppDatabase(context, true)
    private val propertiesDao: PropertiesDao = db.propertiesDao()
    private val propertiesService: PropertiesService = FakeApiDataFactory

    @Before
    fun setUp() {
        FakeApiDataFactory.initFakeApi(context)
    }

    @After
    fun tearDown() {
        runBlocking {
            db.propertiesDao().clearAll()
            db.tenantsDao().clearAll()
            db.clearAllTables()
        }
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {

        val remoteMediator = PropertiesRemoteMediator(
            propertiesService,
            db
        )

        val pagingState = PagingState<Int, PropertyAndTenant>(
            listOf(),
            null,
            PagingConfig(37),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }
}