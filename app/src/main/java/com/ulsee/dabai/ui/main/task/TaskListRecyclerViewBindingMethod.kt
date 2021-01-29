package com.ulsee.dabai.ui.main.task

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("taskItems")
fun bindRecyclerViewWithDataItemList(recyclerView: RecyclerView, result: TaskListResult?) {
    result?.success?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is TaskListAdapter ->
                    submitList(it)
            }
        }
    }
}