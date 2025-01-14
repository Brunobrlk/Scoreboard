package com.bgbrlk.scoreboardbrlk.ui.score

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.databinding.ItemSettingsBinding
import com.bgbrlk.scoreboardbrlk.domain.Setting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewHolder private constructor(private val _binding: ItemSettingsBinding) : RecyclerView.ViewHolder(_binding.root){
    private var scope = CoroutineScope(Dispatchers.Main)
    private val delay = 50L
    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: Setting) {
        val context = _binding.root.context
        _binding.apply {
            imagebuttonMinus.setOnLongClickListener {
                handleLongClick(this, item){ if(item.value > 0) item.value-- }
            }
            imagebuttonMinus.setOnTouchListener { _, event ->
                handleTouch(event, this, item){ if(item.value > 0) item.value-- }
            }
            imagebuttonPlus.setOnLongClickListener {
                handleLongClick(this, item){ item.value++ }
            }
            imagebuttonPlus.setOnTouchListener { _, event ->
                handleTouch(event, this, item){ item.value++ }
            }
            setting = item
            executePendingBindings()
        }
    }

    @Suppress("SameReturnValue")
    private fun handleLongClick(binding: ItemSettingsBinding, item: Setting, operation: () -> Unit): Boolean {
        scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            while (true){
                delay(delay)
                binding.setting = item.apply { operation.invoke() }
            }
        }
        return true
    }

    @Suppress("SameReturnValue")
    private fun handleTouch(event: MotionEvent, binding: ItemSettingsBinding, item: Setting, operation: () -> Unit): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN -> binding.setting = item.apply { operation.invoke() }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> scope.cancel()
        }
        return false
    }

    companion object {
        fun from(parent: ViewGroup): SettingsViewHolder {
            val binding = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SettingsViewHolder(binding)
        }
    }

}

class SettingsAdapter: ListAdapter<Setting, SettingsViewHolder>(SettingDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SettingsViewHolder.from(parent)
    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) = holder.bind(getItem(position))
}

object SettingDiffCallback : DiffUtil.ItemCallback<Setting>(){
    override fun areItemsTheSame(oldItem: Setting, newItem: Setting) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Setting, newItem: Setting) = oldItem == newItem
}
