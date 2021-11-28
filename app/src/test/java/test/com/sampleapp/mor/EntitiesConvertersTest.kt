package test.com.sampleapp.mor

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import test.com.sampleapp.mor.data.cache.entities.status.OccupationStatus
import test.com.sampleapp.mor.data.cache.utilities.converters.EntitiesConverters

class EntitiesConvertersTest {

    private val converters = EntitiesConverters()

    @Test
    fun convertStringStatus_toOccupationStatusEnum_isCorrect() {
        val givenStringStatus = OccupationStatus.INACTIVE.status
        val convertedValue = converters.toOccupationStatus(givenStringStatus)

        assertThat(convertedValue.status, equalTo(givenStringStatus))
    }

    @Test
    fun convertWrongStringStatus_toOccupationStatusEnum_isFallbackToDefault() {
        val wrongStringStatus = "inactivsdfe3"
        val convertedValue = converters.toOccupationStatus(wrongStringStatus)
        val desiredValue = OccupationStatus.UNKNOWN

        assertThat(convertedValue.status, equalTo(desiredValue.status))
    }

    @Test
    fun convertOccupationStatus_toString_isCorrect() {
        val givenStatus = OccupationStatus.OCCUPIED
        val convertedStringStatus = converters.fromOccupationStatus(givenStatus)
        val desiredStatusString = OccupationStatus.OCCUPIED.status

        assertThat(convertedStringStatus, equalTo(desiredStatusString))
    }
}