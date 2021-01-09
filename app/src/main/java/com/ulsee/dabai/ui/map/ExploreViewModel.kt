package com.ulsee.dabai.ui.map

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Size
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.ulsee.dabai.R
import com.ulsee.dabai.data.Result
import com.ulsee.dabai.data.RobotDataSource
import com.ulsee.dabai.data.response.MapInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val TAG = ExploreViewModel::class.java.simpleName

class ExploreViewModel(private val repository: ExploreRepository) : ViewModel() {

    private val _exitCreateMapResult = MutableLiveData<CreateMapResult>()
    val exitCreateMapResult: LiveData<CreateMapResult> = _exitCreateMapResult

    private val _loadDynamicMapResult = MutableLiveData<CreateMapResult>()
    val loadDynamicMapResult: LiveData<CreateMapResult> = _loadDynamicMapResult

    var mMapInfo : MapInfo? = null
    var robotPoint = MutableLiveData<PointF?>()

    fun loadDynamicMap(context: Context, imageView: ImageView) {
        viewModelScope.launch {
            var url = getUrl()
            var oldImageUrl = url
            var oldBitmap: Bitmap?
            Glide.with(context).load(url).into(imageView);

            delay(1000)
            while (true) {
                url = getUrl()
//                Glide
//                    .with(context)
//                    .load(url)
//                    .thumbnail(
//                        Glide // this thumbnail request has to have the same RESULT cache key
//                            .with(context) // as the outer request, which usually simply means
//                            .load(oldImageUrl) // same size/transformation(e.g. centerCrop)/format(e.g. asBitmap)
//                            .fitCenter() // have to be explicit here to match outer load exactly
//                    )
//                    .listener(getRequestListener())
//                    .into(imageView)
                oldImageUrl = url;

                Glide.with(context)
                    .asBitmap()
                    .load(url)
//                    .thumbnail(Glide.with(context).load(oldBitmap).fitCenter())
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            oldBitmap = resource
                            val robotImageSize = Size(12, 12)

                            val bmOverlay = Bitmap.createBitmap(
                                resource.getWidth(),
                                resource.getHeight(),
                                resource.getConfig()
                            )
                            val canvas = Canvas(bmOverlay)
                            canvas.drawBitmap(resource, Matrix(), null)
//                            canvas.drawBitmap(robotBitmap, Matrix(), null)
                            if (robotPoint.value != null) {
                                val robotBitmap = context.resources.getDrawable(R.drawable.ic_launcher_foreground, null).toBitmap(robotImageSize.width, robotImageSize.height)

//                                canvas.drawBitmap(robotBitmap, robotPoint.value!!.x, robotPoint.value!!.y, null)
                                val x = robotPoint.value!!.x - robotImageSize.width/2
                                val y = robotPoint.value!!.y - robotImageSize.height/2
                                canvas.drawBitmap(robotBitmap, x, y, null)
                            }
                            imageView.setImageBitmap(bmOverlay)
                        }

                    })

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
            // TODO
            return true
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
    fun updateMapInfo(data: MapInfo) {
        mMapInfo = data
    }

    fun updateRobotPosition(
        mapContainer: RelativeLayout,
        robotIV: ImageView,
        positionTV: TextView,
        x: Double,
        y: Double,
        dp: Double
    ) {

        positionTV.post { positionTV.text = String.format("(%f, %f)", x, y) }

        if (mMapInfo == null) return
        val resolution = mMapInfo!!.resolution

        // origin
        val originXPixel: Double = 0 - mMapInfo!!.x_origin / resolution
        val originYPixel: Double = mMapInfo!!.height - (0 - mMapInfo!!.y_origin / resolution)
        // robot的xy，轉換成在pgm的圖片的pixel點
        val xPointImagePixel: Double = x / resolution
        val yPointImagePixel: Double = -y / resolution

        robotPoint.postValue(PointF(
            (originXPixel + xPointImagePixel).toFloat(),
            (originYPixel + yPointImagePixel).toFloat()
        ))
        // 根據圖片的 pixel 與 螢幕的顯示 mapContainer 轉換成螢幕上的 pixel點(x,y)
        val containerWidth = mapContainer.measuredWidth.toDouble()
        val containerHeight = mapContainer.measuredHeight.toDouble()
        val xScale: Double = containerWidth / mMapInfo!!.width
        val yScale: Double = containerHeight / mMapInfo!!.height
        val scale = Math.min(xScale, yScale)
        val xOffsetPixel = (containerWidth - mMapInfo!!.width.toDouble() * scale) / 2
        val yOffsetPixel = (containerHeight - mMapInfo!!.height.toDouble() * scale) / 2
        val layoutParams = robotIV.layoutParams as RelativeLayout.LayoutParams
        layoutParams.leftMargin =
            (xOffsetPixel + xPointImagePixel * scale + originXPixel * scale - 3.0 * dp).toInt()
        layoutParams.topMargin =
            (yOffsetPixel + yPointImagePixel * scale + originYPixel * scale - 3.0 * dp).toInt()

        Log.d(
            TAG, String.format(
                "size(%dx%d), pointPixel(%.2f,%.2f), container(%.1fx%.1f), scale(%.1fx%.1f)=%.1f, offset=(%.1f,%.1f)",
                mMapInfo!!.width, mMapInfo!!.height,
                xPointImagePixel, yPointImagePixel,
                containerWidth, containerHeight,
                xScale, yScale, scale,
                xOffsetPixel, yOffsetPixel
            )
        )

        robotIV.post { robotIV.setLayoutParams(layoutParams); robotIV.alpha = 0.5f }
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