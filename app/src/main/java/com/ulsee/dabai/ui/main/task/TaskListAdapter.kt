package com.ulsee.dabai.ui.main.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ulsee.dabai.data.response.Task
import com.ulsee.dabai.databinding.ItemTaskListBinding

class TaskListAdapter(val listener: OnItemClickListener): ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback) {

    interface OnItemClickListener{
        fun onItemClicked(item: Task)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskListBinding.inflate(layoutInflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                val item = getItem(position) as Task
                holder.itemView.setOnClickListener {
                    listener.onItemClicked(item)
                }
                holder.setResult(item)
            }
        }
    }

    class TaskViewHolder(private val binding: ItemTaskListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setResult(dataItem: Task) {

            binding.dataItem = dataItem
            binding.executePendingBindings()
        }
    }
}