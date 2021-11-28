package test.com.sampleapp.mor.utilities.server

import okhttp3.mockwebserver.MockResponse
import test.com.sampleapp.mor.data.api.utils.API_KEY
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

object ApiMockResponsesFactory {

    const val propertiesPage1FileName = "properties.json"
    const val propertiesPage2FileName = "properties2.json"

    @JvmStatic
    val appMockResponses = hashMapOf<String, MockResponse>().also {
        it["/365/properties/?api_key=$API_KEY&page=1"] = createSuccessMockResponse(
            propertiesPage1FileName
        )

        it["/365/properties/?api_key=$API_KEY&page=2"] = createSuccessMockResponse(
            propertiesPage2FileName
        )
    }

    private fun createSuccessMockResponse(jsonFilePath: String, delayMillis: Long = 0) =
        MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBodyDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setBody(MockResponseFileReader(jsonFilePath).content)
}