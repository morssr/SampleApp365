package test.com.sampleapp.mor.data.cache.entities.status

enum class OwnerStatus(val status: String) {
    ACTIVE("active"),
    INACTIVE("inactive"),
    UNKNOWN("unknown");

    companion object {
        private val map = values().associateBy(OwnerStatus::status).withDefault { UNKNOWN }
        fun fromString(type: String) = map.getValue(type)
    }
}