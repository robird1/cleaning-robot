package com.ulsee.dabai.ui.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ulsee.dabai.data.MapCloudDataSource
import com.ulsee.dabai.data.MapCloudRepository


class MapListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapListViewModel::class.java)) {
            return MapListViewModel(
                repository = MapCloudRepository(
                    dataSource = MapCloudDataSource(
                        url = "https://120.78.217.167:5200/v1/"
                    )
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}