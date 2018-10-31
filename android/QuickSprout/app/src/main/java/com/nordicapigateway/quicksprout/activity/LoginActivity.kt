package com.nordicapigateway.quicksprout.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nordicapigateway.quicksprout.R
import com.nordicapigateway.quicksprout.api.IQuickSproutAPI
import com.nordicapigateway.quicksprout.api.NAGWebInterface
import com.nordicapigateway.quicksprout.api.NAGWebInterfaceCallback
import com.nordicapigateway.quicksprout.api.QuickSproutAPI
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONObject

const val LOGIN_MESSAGE_CODE: String = "Login/MESSAGE_CODE"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        QuickSproutAPI(this).init(object: IQuickSproutAPI {
            @SuppressWarnings("SetJavaScriptEnabled")
            override fun onInit(response: Response) {
                nagWebView.visibility = View.INVISIBLE
                nagWebView.webViewClient = webViewClient()
                nagWebView.settings.javaScriptEnabled = true
                nagWebView.addJavascriptInterface(NAGWebInterface(callbackInterface()), "NAGWebInterface")
                val json = JSONObject(response.body()?.string())
                appWebView.addJavascriptInterface(NAGWebInterface(callbackInterface()), "NAGWebInterface")
                WebView.setWebContentsDebuggingEnabled(true)
                appWebView.settings.javaScriptEnabled = true
                appWebView.webViewClient = webViewClient()
                appWebView.loadUrl(json.getString("authUrl"))
            }
        })

    }

    private fun webViewClient(): WebViewClient {
        return object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if(request?.url?.scheme.equals("nagdemoapp")) {
                    val intent = Intent(this@LoginActivity, AccountActivity::class.java).apply {
                        putExtra(LOGIN_MESSAGE_CODE, request?.url.toString())
                    }
                    startActivity(intent)
                    return true
                }
                return false
            }
        }
    }

    private fun callbackInterface(): NAGWebInterfaceCallback {
        return object: NAGWebInterfaceCallback {
            override fun onLoadWebViewContent(data: String) {
                var json = JSONObject(data)
                var html = json.getString("html")
                var baseUrl = json.getString("baseUrl")
                runOnUiThread {
                    appWebView.visibility = View.INVISIBLE
                    nagWebView.visibility = View.VISIBLE
                    nagWebView.loadDataWithBaseURL(baseUrl, html, "text/html", "utf-8", null)
                }
            }

            override fun onSendReturnValue(data: String) {
                var json = JSONObject(data)
                var dataz = json.getString("data")
                runOnUiThread {
                    nagWebView.visibility = View.INVISIBLE
                    appWebView.visibility = View.VISIBLE
                    val javaScript = """window.NAGWebViewBridge.sendReturnValue("$dataz")"""
                    appWebView.evaluateJavascript(javaScript, null)
                }
            }
        }
    }
}
