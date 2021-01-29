package com.ulsee.dabai.ui.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.MapCloudRepository
import kotlinx.coroutines.launch


class MapListViewModel(private val repository: MapCloudRepository) : ViewModel() {

    private val _mapListResult = MutableLiveData<MapListResult>()
    val mapListResult: LiveData<MapListResult> = _mapListResult

    fun getList(projectID: Int) {
        viewModelScope.launch {
            val result = repository.getList(projectID)

            if (result is Result.Success) {
                _mapListResult.value = MapListResult(success = result.data.data)
            } else {
                _mapListResult.value = MapListResult(error = R.string.get_map_list_failed)
            }
        }
    }
}