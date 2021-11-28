package test.com.sampleapp.mor.data.api.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Test

class DateUtilTest {

    @Test
    fun parseStringToDateObject_isNotNull() {
        val dtStart = "2021-08-15T07:14:05.677+00:00"
        val date = DateUtil.parseStringToDateObject(dtStart)

        assertThat(date, `is`(notNullValue()))
    }
}