package test.com.sampleapp.mor.data

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import test.com.sampleapp.mor.data.api.resposes.PropertiesResponse
import androidx.paging.PagingData
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.ui.PropertyStatusFilter
import test.com.sampleapp.mor.ui.TenantStatusFilter

interface PropertiesRepository {

    suspend fun getAllProperties(): List<PropertyAndTenant>

    fun getAllPropertiesFlow(): Flow<List<PropertyAndTenant>>

    fun getPropertiesByStatusFlow(
        propertyStatusFilter: PropertyStatusFilter = PropertyStatusFilter.ACTIVE,
        tenantStatusFilter: TenantStatusFilter = TenantStatusFilter.ANY
    ): Flow<List<PropertyAndTenant>>

    suspend fun getAllPropertiesApi(page: Int): Response<PropertiesResponse>

    fun getPropertiesByStatusPaging(
        propertyStatusFilter: PropertyStatusFilter,
        tenantStatusFilter: TenantStatusFilter
    ): Flow<PagingData<PropertyAndTenant>>

    suspend fun insertPropertiesAndTenant(properties: List<PropertyAndTenant>)
}