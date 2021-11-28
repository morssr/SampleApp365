package test.com.sampleapp.mor.utilities.server

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

class MockWebServer(
    val mockResponses: HashMap<String, MockResponse> = hashMapOf()
) {
    val server = MockWebServer()

    init {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockResponses.getOrElse(request.path!!) {
                    MockResponse().setResponseCode(405)
                }
            }
        }
    }

    fun startServer() {
        server.start()
    }

    fun stopServer() {
        server.shutdown()
    }

    fun getServerBaseUrl() = server.url("").toString()
}