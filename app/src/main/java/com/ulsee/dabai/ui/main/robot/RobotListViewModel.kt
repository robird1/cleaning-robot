package com.ulsee.dabai.ui.main.robot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotCloudRepository
import kotlinx.coroutines.launch

class RobotListViewModel(private val repository: RobotCloudRepository) : ViewModel() {

    private val _robotListResult = MutableLiveData<RobotListResult>()
    val robotListResult: LiveData<RobotListResult> = _robotListResult

    fun getList(projectID: Int) {
        viewModelScope.launch {
            val result = repository.getList(projectID)

            if (result is Result.Success) {
                _robotListResult.value = RobotListResult(success = result.data.data)
            } else {
                _robotListResult.value = RobotListResult(error = R.string.get_robot_list_failed)
            }
        }
    }
}