package test.com.sampleapp.mor.data.cache.utilities.converters

import androidx.room.TypeConverter
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.entities.status.OwnerStatus
import test.com.sampleapp.mor.data.cache.entities.status.TenantStatus

class EntitiesConverters {

    @TypeConverter
    fun fromOccupationStatus(status: OccupationStatus): String = status.status

    @TypeConverter
    fun toOccupationStatus(status: String): OccupationStatus = OccupationStatus.fromString(status)

    @TypeConverter
    fun fromOwnerStatus(status: OwnerStatus): String = status.status

    @TypeConverter
    fun toOwnerStatus(status: String): OwnerStatus = OwnerStatus.fromString(status)

    @TypeConverter
    fun fromTenantStatus(status: TenantStatus): String = status.status

    @TypeConverter
    fun toTenantStatus(status: String): TenantStatus = TenantStatus.fromString(status)
}