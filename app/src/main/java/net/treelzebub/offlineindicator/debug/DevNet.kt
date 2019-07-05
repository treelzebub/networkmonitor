package net.treelzebub.offlineindicator.debug

import android.util.Log
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

object DevNet {
    private const val TAG = "DevNet"

    private val client = OkHttpClient()

    fun call() {
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/todos/1")
            .build()
        Log.d(TAG, "Call: ${request.urlString()}")
        client.newCall(request).enqueue(callback)
    }

    private val callback = object : Callback {
        override fun onFailure(request: Request?, e: IOException?) {
            Log.d(TAG, "Failure: ${e?.message.orEmpty()}")
        }

        override fun onResponse(response: Response) {
            Log.d(TAG, "Success, size: ${response.body().bytes().size}b")
        }
    }
}