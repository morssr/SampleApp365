package test.com.sampleapp.mor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import test.com.sampleapp.mor.utilities.server.MockWebServer
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var server: MockWebServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(IO) {
            server.startServer()
            Log.d(TAG, "onCreate: ${server.getServerBaseUrl()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch(IO) {
            server.stopServer()
        }
    }
}