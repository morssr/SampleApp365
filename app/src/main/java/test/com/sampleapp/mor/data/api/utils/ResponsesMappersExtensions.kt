package test.com.sampleapp.mor.data.api.utils

import test.com.sampleapp.mor.data.api.resposes.PropertyResponse
import test.com.sampleapp.mor.data.api.resposes.TenantResponse
import test.com.sampleapp.mor.data.cache.entities.Property
import test.com.sampleapp.mor.data.cache.entities.Tenant
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.entities.status.OwnerStatus
import test.com.sampleapp.mor.data.cache.entities.status.TenantStatus
import test.com.sampleapp.mor.data.cache.relations.PropertyAndTenant
import java.util.*

fun List<PropertyResponse>.mapToPropertiesAndTenants() =
    arrayListOf<PropertyAndTenant>().apply {
        this@mapToPropertiesAndTenants.forEach { propertyResponse ->
            this.add(propertyResponse.mapToPropertyAndTenant())
        }
    }

fun PropertyResponse.mapToPropertyAndTenant() = PropertyAndTenant(
    mapToPropertyEntity(),
    tenant?.mapToTenantEntity(propertyId)
)

fun PropertyResponse.mapToPropertyEntity() = Property(
    propertyId,
    address,
    owner,
    OwnerStatus.fromString(ownerStatus),
    DateUtil.parseStringToDateObject(createdOn),
    OccupationStatus.fromString(occupiedStats),
    plan
)

fun TenantResponse.mapToTenantEntity(propertyResponse: PropertyResponse) = Tenant(
    contactId,
    propertyResponse.propertyId,
    firstName,
    lastName,
    TenantStatus.fromString(tenantStatus)
)

fun TenantResponse.mapToTenantEntity(propertyId: String) = Tenant(
    contactId,
    propertyId,
    firstName,
    lastName,
    TenantStatus.fromString(tenantStatus)
)

