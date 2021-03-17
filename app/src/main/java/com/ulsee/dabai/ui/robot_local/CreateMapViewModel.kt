package com.ulsee.dabai.ui.robot_local

import android.util.Log
import androidx.lifecycle.*
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotDataSource
import kotlinx.coroutines.launch

private val TAG = CreateMapViewModel::class.java.simpleName

class CreateMapViewModel(private val repository: CreateMapRepository) : ViewModel() {

    private val _createMapForm = MutableLiveData<CreateMapFormState>()
    val createMapFormState: LiveData<CreateMapFormState> = _createMapForm

    private val _createMapResult = MutableLiveData<CreateMapResult>()
    val createMapResult: LiveData<CreateMapResult> = _createMapResult

    fun createMap(mapName: String, mapFloor: String) {
        viewModelScope.launch {
            val result = repository.createMap(mapName, mapFloor)

            if (result is Result.Success) {
                Log.d(TAG, "[Enter] result is Result.Success")
                _createMapResult.value = CreateMapResult(success = R.string.create_map_success)
            } else {
                _createMapResult.value = CreateMapResult(error = R.string.create_map_failed)
            }
        }
    }

    fun loginDataChanged(mapName: String, mapFloor: String) {
        if (!isMapNameValid(mapName)) {
            _createMapForm.value = CreateMapFormState(mapNameError = R.string.invalid_map_name)
        } else if (!isMapFloorValid(mapFloor)) {
            _createMapForm.value = CreateMapFormState(mapFloorError = R.string.invalid_map_floor)
        } else {
            _createMapForm.value = CreateMapFormState(isDataValid = true)
        }
    }

    // A placeholder mapName validation check/;
    private fun isMapNameValid(mapName: String) = mapName.isNotBlank()

    // A placeholder mapFloor validation check
    private fun isMapFloorValid(floor: String) = floor.matches(Regex("-?\\d+(\\.\\d+)?"))
}


class CreateMapViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateMapViewModel::class.java)) {
            return CreateMapViewModel(
                repository = CreateMapRepository(dataSource = RobotDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}