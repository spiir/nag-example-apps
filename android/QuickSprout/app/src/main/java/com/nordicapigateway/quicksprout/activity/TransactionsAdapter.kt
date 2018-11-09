package com.nordicapigateway.quicksprout.activity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nordicapigateway.quicksprout.R
import kotlinx.android.synthetic.main.view_holder_transaction.view.*
import org.json.JSONArray
import org.json.JSONObject


class TransactionsAdapter(private val data: JSONArray) : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    class TransactionsViewHolder(
        val container: LinearLayout)
        : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsAdapter.TransactionsViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_transaction, parent, false) as LinearLayout
        return TransactionsViewHolder(container)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.container.type.text = (data[position] as JSONObject).get("type").toString()
        holder.container.text.text = (data[position] as JSONObject).get("text").toString()
        holder.container.date.text = (data[position] as JSONObject).get("date").toString()
        holder.container.currency.text =  (data[position] as JSONObject).get("amount").toString() + (data[position] as JSONObject).get("currency").toString()
        holder.container.state.text = (data[position] as JSONObject).get("state").toString()
    }

    override fun getItemCount() = data.length()

}