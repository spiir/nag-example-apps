package com.nordicapigateway.quicksprout.api

import android.webkit.JavascriptInterface

@FunctionalInterface
interface NAGWebInterfaceCallback {
    fun onSendReturnValue(data: String) {}
    fun onLoadWebViewContent(data: String) {}
}

class NAGWebInterface(private val callback: NAGWebInterfaceCallback) {
    @JavascriptInterface
    fun loadWebViewContent(data: String) {
        callback.onLoadWebViewContent(data)

    }
    @JavascriptInterface
    fun sendReturnValue(data: String) {
        callback.onSendReturnValue(data)
    }
}