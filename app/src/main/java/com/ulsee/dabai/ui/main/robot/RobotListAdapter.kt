package com.ulsee.dabai.ui.main.robot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ulsee.dabai.data.response.Robot
import com.ulsee.dabai.databinding.ItemRobotListBinding

class RobotListAdapter(val listener: OnItemClickListener): ListAdapter<Robot, RobotListAdapter.RobotViewHolder>(DiffCallback) {

    interface OnItemClickListener{
        fun onItemClicked(item: Robot)
        fun onPosition(item: Robot)
        fun onMap(item: Robot)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Robot>() {

        override fun areItemsTheSame(oldItem: Robot, newItem: Robot): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Robot, newItem: Robot): Boolean {
            return oldItem.robot_id == newItem.robot_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RobotViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRobotListBinding.inflate(layoutInflater, parent, false)
        return RobotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RobotViewHolder, position: Int) {
        when (holder) {
            is RobotViewHolder -> {
                val item = getItem(position) as Robot
                holder.itemView.setOnClickListener {
                    listener.onItemClicked(item)
                }
                holder.setOnPositionCallback(object: RobotListItemViewModel.onPositionCallback{
                    override fun onPosition() {
                        listener.onPosition(item)
                    }
                })
                holder.setOnMapCallback(object: RobotListItemViewModel.onMapCallback{
                    override fun onMap() {
                        listener.onMap(item)
                    }
                })
                holder.setResult(item)
            }
        }
    }

    class RobotViewHolder(private val binding: ItemRobotListBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mViewModel: RobotListItemViewModel = RobotListItemViewModel()

        init {
            binding.viewModel = mViewModel
        }
        fun setOnPositionCallback(cb: RobotListItemViewModel.onPositionCallback) {
            mViewModel.setOnPositionCallback(cb)
        }
        fun setOnMapCallback(cb: RobotListItemViewModel.onMapCallback) {
            mViewModel.setOnMapCallback(cb)
        }
        fun setResult(dataItem: Robot) {
            binding.dataItem = dataItem
            binding.executePendingBindings()
        }
    }
}