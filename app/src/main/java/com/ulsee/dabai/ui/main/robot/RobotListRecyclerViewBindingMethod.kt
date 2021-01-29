package com.ulsee.dabai.ui.main.robot

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("robotItems")
fun bindRecyclerViewWithDataItemList(recyclerView: RecyclerView, result: RobotListResult?) {
    result?.success?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is RobotListAdapter ->
                    submitList(it)
            }
        }
    }
}