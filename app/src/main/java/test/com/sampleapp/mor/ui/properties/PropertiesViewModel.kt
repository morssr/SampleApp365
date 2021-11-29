package test.com.sampleapp.mor.ui.properties

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import test.com.sampleapp.mor.data.PropertiesRepository
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PropertiesRepository
) : ViewModel() {

    private var currentFilteredPropertiesFlow: Flow<PagingData<PropertyAndTenant>>? = null

    private var currentPropertyStatusFilter = PropertyStatusFilter.ACTIVE
    private var currentTenantStatusFilter = TenantStatusFilter.ANY

    fun getPropertiesAndTenantsByStatusPaging(
        propertyStatusFilter: PropertyStatusFilter = PropertyStatusFilter.ACTIVE,
        tenantStatusFilter: TenantStatusFilter = TenantStatusFilter.ANY
    ): Flow<PagingData<PropertyAndTenant>> {

        val lastResultFlow = currentFilteredPropertiesFlow

        //returns cached data if requirements matched
        if (propertyStatusFilter == currentPropertyStatusFilter
            && currentTenantStatusFilter == currentTenantStatusFilter
            && lastResultFlow != null
        ) {
            return lastResultFlow
        }
        val skipInitialize = false

        //create new data stream
        val newResult =
            repository.getPropertiesByStatusPaging(
                propertyStatusFilter,
                tenantStatusFilter,
                skipInitialize
            )
                .cachedIn(viewModelScope)

        //replace the old flow with the new one
        currentFilteredPropertiesFlow = newResult
        return newResult
    }

    fun insertAll(properties: List<PropertyAndTenant>) {
        viewModelScope.launch(IO) {
            repository.insertPropertiesAndTenant(properties)
        }
    }

    suspend fun getPropertiesByStatusFlow() = repository.getPropertiesByStatusFlow()

    suspend fun getAllPropertiesApi(page: Int) = repository.getAllPropertiesApi(page)
}