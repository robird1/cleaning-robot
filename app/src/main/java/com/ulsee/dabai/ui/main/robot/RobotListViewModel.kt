package com.ulsee.dabai.ui.main.robot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotCloudRepository
import com.ulsee.dabai.data.request.PositioningRequest
import com.ulsee.dabai.ui.main.task.ExecuteTaskResult
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

    private val _positioningRobotResult = MutableLiveData<RobotPositioningResult>()
    val positioningRobotResult: LiveData<RobotPositioningResult> = _positioningRobotResult
    fun positioning(projectID: Int, robotID: Int, payload: PositioningRequest) {
        viewModelScope.launch {
            val result = repository.positioning(projectID, robotID, payload)

            if (result is Result.Success) {
                _positioningRobotResult.value = RobotPositioningResult(success = true)
            } else {
                _positioningRobotResult.value = RobotPositioningResult(success = false, error = R.string.positioning_robot_failed)
            }
        }
    }
}