package test.com.sampleapp.mor.utilities.server

import okhttp3.mockwebserver.MockResponse
import test.com.sampleapp.mor.data.api.utils.API_KEY
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object ApiMockResponsesFactory {

    private const val PAGE_LOAD_DELAY = 3000L

    const val propertiesPage1FileName = "properties1.json"
    const val propertiesPage2FileName = "properties2.json"
    const val propertiesPage3FileName = "properties3.json"
    const val propertiesPage4FileName = "properties4.json"
    const val propertiesPage5FileName = "properties5.json"
    const val propertiesPage6FileName = "properties6.json"
    const val propertiesPage7FileName = "properties7.json"

    @JvmStatic
    val appMockResponses = hashMapOf<String, MockResponse>().also {
        it["/365/properties/?api_key=$API_KEY&page=1"] = createSuccessMockResponse(
            propertiesPage1FileName
        )

        it["/365/properties/?api_key=$API_KEY&page=2"] = createSuccessMockResponse(
            propertiesPage2FileName,
            PAGE_LOAD_DELAY
        )

        it["/365/properties/?api_key=$API_KEY&page=3"] = createSuccessMockResponse(
            propertiesPage3FileName,
            PAGE_LOAD_DELAY
        )
        it["/365/properties/?api_key=$API_KEY&page=4"] = createSuccessMockResponse(
            propertiesPage4FileName,
            PAGE_LOAD_DELAY
        )
        it["/365/properties/?api_key=$API_KEY&page=5"] = createSuccessMockResponse(
            propertiesPage5FileName,
            PAGE_LOAD_DELAY
        )
        it["/365/properties/?api_key=$API_KEY&page=6"] = createSuccessMockResponse(
            propertiesPage6FileName,
            PAGE_LOAD_DELAY
        )
        it["/365/properties/?api_key=$API_KEY&page=7"] = createSuccessMockResponse(
            propertiesPage7FileName
        )
    }

    private fun createSuccessMockResponse(jsonFilePath: String, delayMillis: Long = 0) =
        MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBodyDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setBody(MockResponseFileReader(jsonFilePath).content)
}