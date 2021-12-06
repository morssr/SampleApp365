package test.com.sampleapp.mor.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import test.com.sampleapp.mor.data.cache.entities.Tenant
import test.com.sampleapp.mor.data.cache.utilities.converters.QueryHelper

@Dao
interface TenantsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tenant: Tenant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tenants: List<Tenant>)

    @Query("DELETE FROM ${QueryHelper.TENANTS_TABLE_NAME}")
    suspend fun clearAll()
}