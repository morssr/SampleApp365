package test.com.sampleapp.mor.data.cache.utilities.converters

import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.entities.status.TenantStatus
import test.com.sampleapp.mor.ui.properties.PropertyStatusFilter
import test.com.sampleapp.mor.ui.properties.TenantStatusFilter

object QueryHelper {

    const val PROPERTIES_TABLE_NAME = "properties"
    const val TENANTS_TABLE_NAME = "tenants"
    private const val WHITE_SPACE = " "

    //query example - SELECT * FROM properties INNER JOIN tenants ON id = property_id WHERE (occupation_status = 'occupied' OR occupation_status = 'vacant') And (tenant_status = 'inactive')
    @JvmStatic
    fun buildPropertiesByStatusQuery(
        propertyStatus: PropertyStatusFilter = PropertyStatusFilter.ACTIVE,
        tenantStatus: TenantStatusFilter = TenantStatusFilter.ALL
    ): String {

        return StringBuilder().apply {
            //fetch all properties from db
            append("SELECT * FROM $PROPERTIES_TABLE_NAME").append(WHITE_SPACE)

            //if tenant status is not required skip the join - needed because null tenants objects
            if (tenantStatus != TenantStatusFilter.ALL)
                append("INNER JOIN tenants ON property_id = id").append(WHITE_SPACE)

            //filter properties by occupation status
            append("WHERE (occupation_status =").append(WHITE_SPACE)

            when (propertyStatus) {
                PropertyStatusFilter.ACTIVE -> append("'${OccupationStatus.OCCUPIED.status}' OR occupation_status = '${OccupationStatus.VACANT.status}'")
                PropertyStatusFilter.OCCUPIED -> append("'${OccupationStatus.OCCUPIED.status}'")
                PropertyStatusFilter.VACANT -> append("'${OccupationStatus.VACANT.status}'")
                PropertyStatusFilter.INACTIVE -> append("'${OccupationStatus.INACTIVE.status}'")
            }
            append(")").append(WHITE_SPACE)

            //filter properties by tenant status
            if (tenantStatus != TenantStatusFilter.ALL) {
                append("And (tenant_status = ")
                when (tenantStatus) {
                    TenantStatusFilter.ACTIVE -> append("'${TenantStatus.ACTIVE.status}'")
                    TenantStatusFilter.INACTIVE -> append("'${TenantStatus.INACTIVE.status}'")
                    TenantStatusFilter.ALL -> append("")
                }
                append(")")
            }

        }.toString()
    }
}