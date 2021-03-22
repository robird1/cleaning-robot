package com.ulsee.dabai.ui.main.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.data.response.Robot
import com.ulsee.dabai.databinding.ItemMapListBinding
import com.ulsee.dabai.ui.main.robot.RobotListItemViewModel

class MapListAdapter(val listener: OnItemClickListener): ListAdapter<Map, MapListAdapter.MapViewHolder>(DiffCallback) {

    interface OnItemClickListener{
        fun onItemClicked(item: Map)
        fun onUpload(item: Map) // for robot upload local map
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Map>() {

        override fun areItemsTheSame(oldItem: Map, newItem: Map): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Map, newItem: Map): Boolean {
            return oldItem.map_id == newItem.map_id && oldItem.uploadable == newItem.uploadable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMapListBinding.inflate(layoutInflater, parent, false)
        return MapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        when (holder) {
            is MapViewHolder -> {
                val item = getItem(position) as Map
                holder.itemView.setOnClickListener {
                    listener.onItemClicked(item)
                }
                holder.setOnUploadCallback(object: MapListItemViewModel.onUploadCallback{
                    override fun onUpload() {
                        listener.onUpload(item)
                    }
                })
                holder.setResult(item)
            }
        }
    }

    class MapViewHolder(private val binding: ItemMapListBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mViewModel: MapListItemViewModel = MapListItemViewModel()

        init {
            binding.viewModel = mViewModel
        }
        fun setOnUploadCallback(cb: MapListItemViewModel.onUploadCallback) {
            mViewModel.setOnUploadCallback(cb)
        }
        fun setResult(dataItem: Map) {
            binding.dataItem = dataItem
            binding.executePendingBindings()
        }
    }
}