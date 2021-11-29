package test.com.sampleapp.mor.data


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow
import test.com.sampleapp.mor.data.api.PropertiesService
import test.com.sampleapp.mor.data.cache.AppDatabase
import test.com.sampleapp.mor.data.cache.paging.PropertiesRemoteMediator
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.data.cache.utilities.converters.QueryHelper
import test.com.sampleapp.mor.ui.properties.PropertyStatusFilter
import test.com.sampleapp.mor.ui.properties.TenantStatusFilter
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

    override suspend fun getAllPropertiesApi(page: Int) = propertiesService.getProperties(page)

    override fun getPropertiesByStatusPaging(
        propertyStatusFilter: PropertyStatusFilter,
        tenantStatusFilter: TenantStatusFilter,
        skipInitialize: Boolean
    ): Flow<PagingData<PropertyAndTenant>> {

        val filterQuery =
            QueryHelper.buildPropertiesByStatusQuery(propertyStatusFilter, tenantStatusFilter)

        val pagingSourceFactory = {
            appDatabase.propertiesDao().getPropertiesRawQueryPaging(SimpleSQLiteQuery(filterQuery))
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 12, enablePlaceholders = false),
            remoteMediator = PropertiesRemoteMediator(
                propertiesService,
                appDatabase,
                skipInitialize
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun insertPropertiesAndTenant(properties: List<PropertyAndTenant>) {
        appDatabase.propertiesDao().insertAllPropertiesAndTenants(properties)
    }
}