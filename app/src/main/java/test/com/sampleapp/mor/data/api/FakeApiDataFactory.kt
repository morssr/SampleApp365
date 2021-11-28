package test.com.sampleapp.mor.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import test.com.sampleapp.mor.data.api.resposes.PropertiesResponse
import test.com.sampleapp.mor.data.api.resposes.PropertyResponse
import test.com.sampleapp.mor.data.api.utils.NullToEmptyStringAdapter
import test.com.sampleapp.mor.data.api.utils.mapToPropertiesAndTenants
import test.com.sampleapp.mor.utilities.readAssetsFile

object FakeApiDataFactory {

    const val propertiesPage1FileName = "properties.json"
    const val propertiesPage2FileName = "properties2.json"

    val propertiesJsonMap = hashMapOf<Int, String>().also {
        it[1] = propertiesPage1FileName
        it[2] = propertiesPage2FileName
    }

    fun getPropertiesJsonFile(context: Context, fileName: String) =
        context.assets.readAssetsFile(fileName)

    fun getPropertiesList(
        context: Context,
        files: HashMap<Int, String> = propertiesJsonMap,
        page: Int
    ): List<PropertyResponse>? {
        val jsonConverter = buildMoshiConverter()

        val fileName = when (page) {
            1 -> files.getValue(1)
            2 -> files.getValue(2)
            else -> throw Exception("file page '$page' not exist")
        }
        return jsonConverter.fromJson(getPropertiesJsonFile(context, fileName))?.properties
    }

    fun getFirstPagePropertiesAndTenants(context: Context) =
        getPropertiesList(context = context, page = 1)
            ?.mapToPropertiesAndTenants()!!

    fun getSecondPagePropertiesAndTenants(context: Context) =
        getPropertiesList(context = context, page = 2)
            ?.mapToPropertiesAndTenants()!!

    private fun buildMoshiConverter() = Moshi.Builder()
        .add(NullToEmptyStringAdapter())
        .add(KotlinJsonAdapterFactory())
        .build().adapter(PropertiesResponse::class.java)

}

