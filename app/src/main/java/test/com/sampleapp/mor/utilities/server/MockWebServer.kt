package test.com.sampleapp.mor.utilities.server

import android.util.Log
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

private const val TAG = "MockWebServer"

class MockWebServer(
    val mockResponses: HashMap<String, MockResponse> = hashMapOf()
) {
    val server = MockWebServer()
    var started = false
        private set

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
        if (started) {
            Log.w(TAG, "startServer: server already started!")
            return
        }
        started = true
        server.start()
    }

    fun stopServer() {
        started = false
        server.shutdown()
    }

    fun getServerBaseUrl() = server.url("").toString()
}