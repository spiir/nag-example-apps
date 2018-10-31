package com.nordicapigateway.quicksprout.activity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.nordicapigateway.quicksprout.R
import com.nordicapigateway.quicksprout.api.IQuickSproutAPI
import com.nordicapigateway.quicksprout.api.QuickSproutAPI
import org.json.JSONObject

class AccountActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val message = intent.getStringExtra(LOGIN_MESSAGE_CODE)
        val uri = Uri.parse(message)
        if(uri.queryParameterNames.contains("code")) {
            QuickSproutAPI(this).tokens(uri.getQueryParameter("code"), object: IQuickSproutAPI{
                override fun onToken(token: String) {
                    QuickSproutAPI(this@AccountActivity).accounts(token, object: IQuickSproutAPI{
                        override fun onAccount(accountsObject: JSONObject) {
                            val accounts = accountsObject.getJSONArray("accounts")
                            viewManager = LinearLayoutManager(this@AccountActivity)
                            viewAdapter = AccountAdapter(accounts)
                            recyclerView = this@AccountActivity.findViewById<RecyclerView>(R.id.recyclerView).apply {
                                setHasFixedSize(true)
                                layoutManager = viewManager
                                adapter = viewAdapter
                            }
                        }
                    })
                }
            })
        } else if(uri.queryParameterNames.contains("success")) {
            Log.d(LOGIN_MESSAGE_CODE, uri.getQueryParameter("success"))
        }
    }
}
