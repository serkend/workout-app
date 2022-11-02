package com.example.a5_workout_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a5_workout_app.databinding.ItemHistoryBinding

class HistoryAdapter(var itemList: List<HistoryEntity>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(itemBinding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var counter = itemBinding.counterHistoryTV
        val date = itemBinding.dateHistoryTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.counter.text ="Exercise " + (position + 1 ).toString()
        holder.date.text = itemList[position].date
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}