package test.com.sampleapp.mor.data.cache.entities.status

enum class TenantStatus(val status: String) {
    ACTIVE("active"),
    INACTIVE("inactive"),
    UNKNOWN("unknown");

    companion object {
        private val map = values().associateBy(TenantStatus::status).withDefault { UNKNOWN }
        fun fromString(type: String) = map.getValue(type)
    }
}