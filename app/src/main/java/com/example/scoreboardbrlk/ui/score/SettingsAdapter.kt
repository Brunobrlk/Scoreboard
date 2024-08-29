package com.example.scoreboardbrlk.ui.score

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboardbrlk.databinding.ItemSettingsBinding
import com.example.scoreboardbrlk.domain.Setting

class SettingsAdapter(private val settingList: List<Setting>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val binding = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SettingsViewHolder -> {
                holder.bind(position)
            }
        }
    }
    override fun getItemCount() = settingList.size

    fun getList() = settingList

    inner class SettingsViewHolder(private val binding: ItemSettingsBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(position: Int){
            val item = settingList[position]
            binding.apply {
                imageViewSettingIcon.setImageResource(item.icon)
                textviewSettingTitle.text = item.name
                textviewSettingCounter.text = item.counter.toString()
                imagebuttonMinus.setOnClickListener {
                    settingList[position].counter--
                    notifyDataSetChanged()
                }
                imagebuttonPlus.setOnClickListener {
                    settingList[position].counter++
                    notifyDataSetChanged()
                }
            }
        }
    }
}

interface SaveClickInterface {
    fun onSaveClick(settingList: List<Setting>)
}