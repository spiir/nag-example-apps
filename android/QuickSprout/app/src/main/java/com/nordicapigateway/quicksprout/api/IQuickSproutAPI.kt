package com.nordicapigateway.quicksprout.api

import okhttp3.Response
import org.json.JSONObject

@FunctionalInterface
interface IQuickSproutAPI {
    fun onInit(response: Response) {}
    fun onPaymentCreate(response: Response) {}
    fun onToken(token: String) {}
    fun onAccount(accountsObject: JSONObject) {}
    fun onTransactions(transactionObject: JSONObject) {}
}