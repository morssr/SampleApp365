package test.com.sampleapp.mor.data.cache.entities.status

enum class OccupationStatus(val status: String) {
    OCCUPIED("occupied"),
    VACANT("vacant"),
    INACTIVE("inactive"),
    UNKNOWN("unknown");

    companion object {
        private val map = values().associateBy(OccupationStatus::status).withDefault { UNKNOWN }
        fun fromString(type: String) = map.getValue(type)
    }
}