package test.com.sampleapp.mor.data

import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow
import test.com.sampleapp.mor.data.api.PropertiesService
import test.com.sampleapp.mor.data.cache.AppDatabase
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.data.cache.utilities.converters.QueryHelper
import test.com.sampleapp.mor.ui.PropertyStatusFilter
import test.com.sampleapp.mor.ui.TenantStatusFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val propertiesService: PropertiesService,
    private val appDatabase: AppDatabase
) : PropertiesRepository {

    override suspend fun getAllProperties(): List<PropertyAndTenant> =
        appDatabase.propertiesDao().getAllProperties()

    override fun getAllPropertiesFlow(): Flow<List<PropertyAndTenant>> =
        appDatabase.propertiesDao().getAllPropertiesFlow()

    override fun getPropertiesByStatusFlow(
        propertyStatusFilter: PropertyStatusFilter,
        tenantStatusFilter: TenantStatusFilter
    ): Flow<List<PropertyAndTenant>> = appDatabase.propertiesDao().getPropertiesRawQueryFlow(
        SimpleSQLiteQuery(
            QueryHelper.buildPropertiesByStatusQuery(
                propertyStatusFilter,
                tenantStatusFilter
            )
        )
    )

    override suspend fun insertPropertiesAndTenant(properties: List<PropertyAndTenant>) {
        appDatabase.propertiesDao().insertAllPropertiesAndTenants(properties)
    }

    override suspend fun getAllPropertiesApi(page: Int) = propertiesService.getPopularMovies(page)
}