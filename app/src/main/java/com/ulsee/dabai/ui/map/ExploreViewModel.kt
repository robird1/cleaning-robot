package com.ulsee.dabai.ui.map

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val TAG = ExploreViewModel::class.java.simpleName

class ExploreViewModel(private val repository: ExploreRepository) : ViewModel() {

    private val _exitCreateMapResult = MutableLiveData<CreateMapResult>()
    val exitCreateMapResult: LiveData<CreateMapResult> = _exitCreateMapResult

    private val _loadDynamicMapResult = MutableLiveData<CreateMapResult>()
    val loadDynamicMapResult: LiveData<CreateMapResult> = _loadDynamicMapResult

    fun loadDynamicMap(context: Context, imageView: ImageView) {
        viewModelScope.launch {
//            val result = repository.loadDynamicMap(getCurrentTime())
//
//            if (result is Result.Success) {
//                Log.d(TAG, "[Enter] result is Result.Success")
////                _loadDynamicMapResult.value = CreateMapResult(success = R.string.create_map_success)
//            } else {
////                _loadDynamicMapResult.value = CreateMapResult(error = R.string.create_map_failed)
//            }

            val url = "http://192.168.10.5:9980/tmp/00000000/00000000.png?t="+ System.currentTimeMillis()
            Glide.with(context).load(url).into(imageView);

            delay(1000)
            while(true) {
//                Log.d(TAG, "[Enter] Glide.with(context).load(url).into(imageView)")
                val url = "http://192.168.10.5:9980/tmp/00000000/00000000.png?t="+ System.currentTimeMillis()
                Glide.with(context).load(url).skipMemoryCache(false).into(imageView);

                delay(1000)
            }

        }
    }

    fun exitCreateMap(mapName: String, mapFloor: String) {
        viewModelScope.launch {
            val result = repository.exitCreateMap(mapName, mapFloor)

            if (result is Result.Success) {
                Log.d(TAG, "[Enter] result is Result.Success")
                // TODO　refactor
                _exitCreateMapResult.value = CreateMapResult(success = R.string.create_map_success)
            } else {
                _exitCreateMapResult.value = CreateMapResult(error = R.string.create_map_failed)
            }
        }
    }


    private fun getCurrentTime() = System.currentTimeMillis()


}


class ExploreViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(
                repository = ExploreRepository(dataSource = RobotDataSource())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}