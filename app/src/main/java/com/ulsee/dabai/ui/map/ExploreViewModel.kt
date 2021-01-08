package com.ulsee.dabai.ui.map

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
            var url = getUrl()
            var oldImageUrl = url
            Glide.with(context).load(url).into(imageView);

            delay(1000)
            while (true) {
                url = getUrl()
                Glide
                    .with(context)
                    .load(url)
                    .thumbnail(Glide // this thumbnail request has to have the same RESULT cache key
                        .with(context) // as the outer request, which usually simply means
                        .load(oldImageUrl) // same size/transformation(e.g. centerCrop)/format(e.g. asBitmap)
                        .fitCenter() // have to be explicit here to match outer load exactly
                    )
                    .listener(getRequestListener())
                    .into(imageView)
                oldImageUrl = url;

                delay(1000)
            }
        }
    }

    private fun getUrl() = "http://192.168.10.5:9980/tmp/00000000/00000000.png?t=" + System.currentTimeMillis()


    private fun getRequestListener() = object : RequestListener<Drawable> { //9
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (isFirstResource) {
                return false; // thumbnail was not shown, do as usual
            }
            return true
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