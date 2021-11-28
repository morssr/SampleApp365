package test.com.sampleapp.mor.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.entities.status.OwnerStatus
import java.util.*

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey
    val id: String,
    val address: String,
    val owner: String,
    @ColumnInfo(name = "owner_status")
    val ownerStatus: OwnerStatus,
    @ColumnInfo(name = "creation_date")
    val creationDate: Date?,
    @ColumnInfo(name = "occupation_status")
    val occupationStatus: OccupationStatus,
    val plan: String
)