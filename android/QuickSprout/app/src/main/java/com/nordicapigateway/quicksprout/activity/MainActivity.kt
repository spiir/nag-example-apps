package com.nordicapigateway.quicksprout.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nordicapigateway.quicksprout.R
import com.nordicapigateway.quicksprout.api.IQuickSproutAPI
import com.nordicapigateway.quicksprout.api.QuickSproutAPI
import com.nordicapigateway.sdk.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Response
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            QuickSproutAPI(this).init(object: IQuickSproutAPI {
                override fun onInit(response: Response) {
                    val json = JSONObject(response.body()?.string())
                    startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java).apply {
                        putExtra("html", json.getString("authUrl"))
                    }, 0)
                }
            })
            startActivity(intent)

        }

        payment_button.setOnClickListener {
            QuickSproutAPI(this).createPayment(object: IQuickSproutAPI {
                override fun onPaymentCreate(response: Response) {
                    val json = JSONObject(response.body()?.string())
                    startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java).apply {
                        putExtra("html", json.getString("redirectUrl"))
                    }, 0)
                }
            })
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                data.let {
                    val code = it?.getStringExtra("code")
                    code?.let {
                        QuickSproutAPI(this).tokens(it, object: IQuickSproutAPI{
                            override fun onToken(token: String) {
                                startActivity(Intent(this@MainActivity, AccountActivity::class.java).apply {
                                    putExtra("accessToken", token)
                                })
                            }
                        })
                    }
                    val error = it?.getStringExtra("error")
                    error?.let {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Activity cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    }
}