package com.example.gestaofinanceira.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaofinanceira.Models.User
import com.example.gestaofinanceira.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class FinancesAdapter(var user: User?): RecyclerView.Adapter<FinancesAdapter.FinancesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_finances_layout, parent, false)
        return FinancesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user?.financeActivity?.size ?: 0
    }

    override fun onBindViewHolder(holder: FinancesViewHolder, position: Int) {
        val activity = user?.financeActivity?.get(position)

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/YYYY")
        holder.itemDate.text = dateFormat.format(activity?.date!!)
        holder.itemType.text = activity.type
        holder.itemDescription.text = "Descrição: " + activity.description
        holder.itemValue.text = "Valor: " + activity.value.toString()
    }

    class FinancesViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemValue: TextView = view.findViewById(R.id.item_textView_value)
        val itemDescription: TextView = view.findViewById(R.id.item_textView_description)
        val itemType: TextView = view.findViewById(R.id.item_textView_type)
        val itemDate: TextView = view.findViewById(R.id.item_textView_date)
    }
}