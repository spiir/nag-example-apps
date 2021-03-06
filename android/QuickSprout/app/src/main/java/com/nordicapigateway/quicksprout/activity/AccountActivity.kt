package com.nordicapigateway.quicksprout.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.nordicapigateway.quicksprout.R
import com.nordicapigateway.quicksprout.adapter.AccountAdapter
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
        supportActionBar?.title = "Accounts"
        intent.getStringExtra("accessToken")?.let {
            QuickSproutAPI(this@AccountActivity).accounts(it, object: IQuickSproutAPI{
                override fun onAccount(accountsObject: JSONObject) {
                    val accounts = accountsObject.getJSONArray("accounts")
                    viewManager = LinearLayoutManager(this@AccountActivity)
                    viewAdapter = AccountAdapter(accounts, it)
                    recyclerView = this@AccountActivity.findViewById<RecyclerView>(R.id.recyclerView).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
            })
        }
    }
}
