package com.thelis.budget

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private var transction: List<Transction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {


    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.label)
        val amount: TextView = view.findViewById(R.id.amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transction = transction[position]
        val context = holder.amount.context
        if (transction.amount >= 0) {
            holder.amount.text = "+ %.2f ₽".format(transction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.grengren))
        } else {
            holder.amount.text = "- %.2f ₽".format(Math.abs(transction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        holder.label.text = transction.label

        holder.itemView.setOnClickListener {
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("transaction",transction)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transction.size
    }

    fun setData(transctions: List<Transction>){
        this.transction = transctions
        notifyDataSetChanged()
    }

}