package com.ulsee.dabai.ui.robot_local

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ulsee.dabai.data.MapLocalDataSource
import com.ulsee.dabai.data.MapLocalRepository

class LocalMapListViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocalMapListViewModel::class.java)) {
            return LocalMapListViewModel(
                    repository = MapLocalRepository(
                            dataSource = MapLocalDataSource(
                                    url = "http://192.168.10.5:9980/"
                            )
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}