package test.com.sampleapp.mor.data.api.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun parseStringToDateObject(date: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return try {
            format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }
}