package com.example.myapplication

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapplication.databinding.ItemStatusBinding

class StatusAdapter(private val activity: Activity): Adapter<StatusAdapter.ViewHolder>() {
    private var list: MutableList<String> = mutableListOf()

    fun addItem(string: String) {
        list.add(0, string)
        notifyItemInserted(0)
    }

    inner class ViewHolder(private val binding: ItemStatusBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.txtStatus.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatusBinding.inflate(activity.layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}