package com.ulsee.dabai.ui.main.robot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ulsee.dabai.data.RobotCloudDataSource
import com.ulsee.dabai.data.RobotCloudRepository

class RobotListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RobotListViewModel::class.java)) {
            return RobotListViewModel(
                    repository = RobotCloudRepository(
                            dataSource = RobotCloudDataSource(
                                    url = "https://120.78.217.167:5200/v1/"
                            )
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}