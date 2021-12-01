package test.com.sampleapp.mor.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import test.com.sampleapp.mor.data.api.resposes.PropertiesResponse
import test.com.sampleapp.mor.data.api.resposes.PropertyResponse
import test.com.sampleapp.mor.data.api.utils.NullToEmptyStringAdapter
import test.com.sampleapp.mor.data.api.utils.mapToPropertiesAndTenants
import test.com.sampleapp.mor.utilities.readAssetsFile
import java.io.InputStreamReader

object FakeApiDataFactory : PropertiesService {

    const val propertiesPage1FileName = "properties1.json"
    const val propertiesPage2FileName = "properties2.json"
    const val propertiesPage3FileName = "properties3.json"

    val propertiesJsonFileNameMap = hashMapOf<Int, String>().also {
        it[1] = propertiesPage1FileName
        it[2] = propertiesPage2FileName
    }

    var propertiesJsonFilesMap: HashMap<Int, String>? = null
        private set
        get() {
            if (field == null) throw UninitializedPropertyAccessException("call initFakeApi() first.")
            return field
        }

    @JvmStatic
    fun initFakeApi(context: Context) {
        propertiesJsonFilesMap = hashMapOf<Int, String>().also {
            it[1] = getPropertiesJsonFile(propertiesPage1FileName)
            it[2] = getPropertiesJsonFile(propertiesPage2FileName)
        }
    }

    fun getPropertiesJsonFile(fileName: String) : String{
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(fileName))
        val content = reader.readText()
        reader.close()
        return content
    }

    fun getPropertiesList(
        context: Context,
        files: HashMap<Int, String> = propertiesJsonFileNameMap,
        page: Int
    ): List<PropertyResponse>? {
        val jsonConverter = buildMoshiConverter()

        val fileName = when (page) {
            1 -> files.getValue(1)
            2 -> files.getValue(2)
            else -> throw Exception("file page '$page' not exist")
        }
        return jsonConverter.fromJson(getPropertiesJsonFile(fileName))?.properties
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

    override suspend fun getProperties(page: Int): Response<PropertiesResponse> =
        Response.success(buildMoshiConverter().fromJson(propertiesJsonFilesMap!![page])
    )
}

