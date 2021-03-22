package com.ulsee.dabai.ui.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.MapCloudRepository
import com.ulsee.dabai.data.request.UploadMapRequest
import kotlinx.coroutines.launch


class MapListViewModel(private val repository: MapCloudRepository) : ViewModel() {

    private val _projectMapListResult = MutableLiveData<MapListResult>()
    val projectMapListResult: LiveData<MapListResult> = _projectMapListResult

    fun getProjectMapList(projectID: Int) {
        viewModelScope.launch {
            val result = repository.getProjectMapList(projectID)

            if (result is Result.Success) {
                _projectMapListResult.value = MapListResult(success = result.data.data)
            } else {
                _projectMapListResult.value = MapListResult(error = R.string.get_map_list_failed)
            }
        }
    }

    private val _robotMapListResult = MutableLiveData<MapListResult>()
    val robotMapListResult: LiveData<MapListResult> = _robotMapListResult

    fun getRobotMapList(projectID: Int, robotID: Int) {
        viewModelScope.launch {
            val result = repository.getRobotMapList(projectID, robotID)

            if (result is Result.Success) {
                _robotMapListResult.value = MapListResult(success = result.data.data)
            } else {
                _robotMapListResult.value = MapListResult(error = R.string.get_map_list_failed)
            }
        }
    }

    private val _uploadMapResult = MutableLiveData<UploadMapResult>()
    val uploadMapResult: LiveData<UploadMapResult> = _uploadMapResult
    fun upload(projectID: Int, mapID: Int, payload: UploadMapRequest) {
        viewModelScope.launch {
            val result = repository.upload(projectID, mapID, payload)

            if (result is Result.Success) {
                _uploadMapResult.value = UploadMapResult(success = true)
            } else {
                _uploadMapResult.value = UploadMapResult(success = false, error = R.string.upload_map_failed)
            }
        }
    }
}