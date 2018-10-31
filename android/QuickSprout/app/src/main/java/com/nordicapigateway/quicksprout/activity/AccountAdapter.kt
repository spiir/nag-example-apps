package com.nordicapigateway.quicksprout.activity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nordicapigateway.quicksprout.R
import kotlinx.android.synthetic.main.view_holder_account.view.*
import org.json.JSONArray
import org.json.JSONObject


class AccountAdapter(private val data: JSONArray) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    class AccountViewHolder(
        val container: LinearLayout)
        : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdapter.AccountViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_account, parent, false) as LinearLayout
        return AccountViewHolder(container)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.container.providerId.text = (data[position] as JSONObject).get("providerId").toString()
        holder.container.name.text = (data[position] as JSONObject).get("name").toString()
        holder.container.iban.text = ((data[position] as JSONObject).get("number") as JSONObject).getString("iban")
        holder.container.currency.text = (data[position] as JSONObject).get("currency").toString()
        holder.container.booked.text = (data[position] as JSONObject).get("bookedBalance").toString()
    }

    override fun getItemCount() = data.length()

}