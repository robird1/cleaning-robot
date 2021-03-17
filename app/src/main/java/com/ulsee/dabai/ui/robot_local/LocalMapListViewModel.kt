package com.ulsee.dabai.ui.robot_local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulsee.dabai.R
import com.ulsee.dabai.data.MapLocalRepository
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.ui.main.map.MapListResult
import kotlinx.coroutines.launch

class LocalMapListViewModel(private val repository: MapLocalRepository) : ViewModel() {

    private val _mapListResult = MutableLiveData<MapListResult>()
    val mapListResult: LiveData<MapListResult> = _mapListResult

    fun getList() {
        viewModelScope.launch {
            val result = repository.getList()

            if (result is Result.Success) {
                _mapListResult.value = MapListResult(success = result.data.data)
            } else {
                _mapListResult.value = MapListResult(error = R.string.get_map_list_failed)
            }
        }
    }
}