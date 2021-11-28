package test.com.sampleapp.mor.data.cache.relations

import androidx.room.Embedded
import androidx.room.Relation
import test.com.sampleapp.mor.data.cache.entities.Property
import test.com.sampleapp.mor.data.cache.entities.Tenant

data class PropertyAndTenant(
    @Embedded val property: Property,
    @Relation(
        parentColumn = "id",
        entityColumn = "property_id"
    )
    val tenant: Tenant?
)