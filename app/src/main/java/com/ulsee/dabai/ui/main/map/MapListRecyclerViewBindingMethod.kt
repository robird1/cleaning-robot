package com.ulsee.dabai.ui.main.map

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("mapItems")
fun bindRecyclerViewWithDataItemList(recyclerView: RecyclerView, result: MapListResult?) {
    result?.success?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is MapListAdapter ->
                    submitList(it)
            }
        }
    }
}