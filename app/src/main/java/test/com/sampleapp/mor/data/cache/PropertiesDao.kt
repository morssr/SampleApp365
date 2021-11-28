package test.com.sampleapp.mor.data.cache

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import test.com.sampleapp.mor.data.cache.entities.Property
import test.com.sampleapp.mor.data.cache.entities.Tenant
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import test.com.sampleapp.mor.data.cache.utilities.converters.QueryHelper

@Dao
interface PropertiesDao {

    @Transaction
    @Query("SELECT * FROM ${QueryHelper.PROPERTIES_TABLE_NAME}")
    suspend fun getAllProperties(): List<PropertyAndTenant>

    @Transaction
    @Query("SELECT * FROM ${QueryHelper.PROPERTIES_TABLE_NAME}")
    fun getAllPropertiesFlow(): Flow<List<PropertyAndTenant>>

    @Transaction
    @RawQuery(observedEntities = [Property::class, Tenant::class])
    fun getPropertiesRawQuery(query: SupportSQLiteQuery): List<PropertyAndTenant>

    @Transaction
    @RawQuery(observedEntities = [Property::class, Tenant::class])
    fun getPropertiesRawQueryFlow(query: SupportSQLiteQuery): Flow<List<PropertyAndTenant>>

   @Transaction
    @RawQuery(observedEntities = [Property::class, Tenant::class])
    fun getPropertiesRawQueryPaging(query: SupportSQLiteQuery): PagingSource<Int, PropertyAndTenant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: Tenant)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(property: PropertyAndTenant) {
        insert(property.property)
        property.tenant?.let {
            insertTenant(it)
        }
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPropertiesAndTenants(propertiesAndTenants: List<PropertyAndTenant>) {
        val properties = arrayListOf<Property>()
        val tenants = arrayListOf<Tenant>()

        propertiesAndTenants.forEach { it ->
            properties.add(it.property)
            it.tenant?.let { tenant -> tenants.add(tenant) }
        }

        insertAll(properties)
        insertAllTenants(tenants)

    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(property: List<Property>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTenants(tenants: List<Tenant>)

    @Query("DELETE FROM properties")
    suspend fun clearAll()
}