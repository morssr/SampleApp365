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

private const val TAG = "PropertiesViewModel"

@HiltViewModel
class PropertiesViewModel @Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PropertiesRepository
) : ViewModel() {

    private var currentFilteredPropertiesFlow: Flow<PagingData<PropertyAndTenant>>? = null

    var currentPropertyStatusFilter = PropertyStatusFilter.ACTIVE
        private set
    var currentTenantStatusFilter = TenantStatusFilter.ALL
        private set

    fun getPropertiesAndTenantsByStatusPaging(
        propertyStatusFilter: PropertyStatusFilter = PropertyStatusFilter.ACTIVE,
        tenantStatusFilter: TenantStatusFilter = TenantStatusFilter.ALL
    ): Flow<PagingData<PropertyAndTenant>> {

        val lastResultFlow = currentFilteredPropertiesFlow

        //returns cached data if requirements matched
        if (propertyStatusFilter == currentPropertyStatusFilter
            && tenantStatusFilter == currentTenantStatusFilter
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
        updateStatus(propertyStatusFilter, tenantStatusFilter)

        return newResult
    }

    private fun updateStatus(
        propertyStatusFilter: PropertyStatusFilter,
        tenantStatusFilter: TenantStatusFilter
    ) {
        currentPropertyStatusFilter = propertyStatusFilter
        currentTenantStatusFilter = tenantStatusFilter
    }

    fun insertAll(properties: List<PropertyAndTenant>) {
        viewModelScope.launch(IO) {
            repository.insertPropertiesAndTenant(properties)
        }
    }

    fun getPropertiesByStatusFlow() = repository.getPropertiesByStatusFlow()

    private suspend fun getAllPropertiesApi(page: Int) = repository.getAllPropertiesApi(page)
}