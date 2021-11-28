package test.com.sampleapp.mor.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import test.com.sampleapp.mor.data.cache.entities.status.TenantStatus

@Entity(tableName = "tenants")
data class Tenant(
    @PrimaryKey
    @ColumnInfo(name = "contact_id")
    val contactId: String,
    @ColumnInfo(name = "property_id")
    val propertyId: String,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "tenant_status")
    val tenantStatus: TenantStatus
)