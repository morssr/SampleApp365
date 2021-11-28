package test.com.sampleapp.mor.data.cache.utilities.converters

import androidx.annotation.Keep
import androidx.room.TypeConverter
import java.util.*

@Keep
class DateTypeConverter {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time
    }
}