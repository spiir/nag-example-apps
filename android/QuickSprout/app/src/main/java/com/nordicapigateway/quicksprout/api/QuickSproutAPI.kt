package com.nordicapigateway.quicksprout.api

import android.support.v7.app.AppCompatActivity
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class QuickSproutAPI constructor(activity: AppCompatActivity) {

    private val activity: AppCompatActivity = activity
    private val url = "http://10.0.2.2:3000"

    fun init(callback: IQuickSproutAPI) {
        val client = OkHttpClient()

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, """{"redirectUrl": "nagdemoapp://"}""")
        val request = Request.Builder()
            .url("""$url/init""")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("API", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                activity.runOnUiThread {
                    callback.onInit(response)
                }
            }
        })
    }

    fun createPayment(callback: IQuickSproutAPI) {
        val client = OkHttpClient()

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, """{"redirectUrl": "nagdemoapp://"}""")
        val request = Request.Builder()
            .url("""$url/create-payment""")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("API", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                activity.runOnUiThread {
                    callback.onPaymentCreate(response)
                }
            }
        })
    }

    fun tokens(code:String, callback: IQuickSproutAPI) {
        val client = OkHttpClient()
        val body = RequestBody.create(MediaType.parse("application/json"), """{"code" : "$code" }""")
        val request = Request.Builder()
            .url("""$url/tokens""")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("API", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONObject(response.body()?.string())
                val token = json.getJSONObject("session").getString("accessToken")
                activity.runOnUiThread {
                    callback.onToken(token)
                }
            }
        })
    }

    fun accounts(token: String, callback: IQuickSproutAPI, activity: AppCompatActivity = this.activity) {
        val client = OkHttpClient()
        val body = RequestBody.create(MediaType.parse("application/json"), """{"token" : "$token" }""")
        val request = Request.Builder()
            .url("""$url/accounts""")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("accounts", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val accountsObject = JSONObject(response.body()?.string())
                activity.runOnUiThread {
                    callback.onAccount(accountsObject)
                }
            }
        })
    }

    fun transactions(token: String, accountId: String, callback: IQuickSproutAPI, activity: AppCompatActivity = this.activity, pagingToken: String? = null) {
        val client = OkHttpClient()
        val body = RequestBody.create(MediaType.parse("application/json"), """{"token" : "$token" }""")
        var url = "$url/accounts/transactions?id=$accountId"
        if (pagingToken != null) url += "&pagingToken=$pagingToken"
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("cache-control", "no-cache")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("transactions", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val accountsObject = JSONObject(response.body()?.string())
                activity.runOnUiThread {
                    callback.onTransactions(accountsObject)
                }
            }
        })
    }
}