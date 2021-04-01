package com.ulsee.dabai.ui.main.robot.positioning

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ulsee.dabai.R
import com.ulsee.dabai.data.*
import com.ulsee.dabai.data.request.PositioningRequest
import com.ulsee.dabai.data.request.PositioningRequestPose
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.ui.main.robot.RobotListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapPositioningFragment : Fragment() {

    val TAG = "MapPositioningFragment"

    var map : Map? = null
    var projectID = 0
    var robotID = 0

    var iv: ImageView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_positioning, container, false)
    }

    override fun onStart() {
        super.onStart()

        val repository = MapCloudRepository(
                dataSource = MapCloudDataSource(
                        url = "https://120.78.217.167:5200/v1/"
                )
        )

        val robotRepository = RobotCloudRepository(
                dataSource = RobotCloudDataSource(
                        url = "https://120.78.217.167:5200/v1/"
                )
        )

        iv = view?.findViewById<ImageView>(R.id.imageView)

        iv?.setOnTouchListener { view, motionEvent ->
            // 1. 取得圖片寬高
            Log.i(TAG, "view ${view.width} x ${view.height}")
            // 2. 取得點擊位置
            Log.i(TAG, "point ${motionEvent.x} x ${motionEvent.y}")

            // 3. 計算位置
            val originX = (0.0-map!!.map_info.x_origin)
            val originY = map!!.map_info!!.height * map!!.map_info.resolution - (0.0 - map!!.map_info!!.y_origin)

            val x = motionEvent.x/view.width*map!!.map_info.width*map!!.map_info.resolution - originX
            val y = 0 - (motionEvent.y/view.height*map!!.map_info.height*map!!.map_info.resolution - originY)
            Log.i(TAG, "result ${x} x ${y}")

            // 4. 定位
            val payload = PositioningRequest(map_id = map!!.map_id, pose = PositioningRequestPose(x, y, 0))
            lifecycleScope.launch {
                val result = robotRepository.positioning(projectID, robotID, payload)
                if (result is Result.Success) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, R.string.positioning_robot_succeed, Toast.LENGTH_LONG).show()
                        activity?.finish()
                    }
                } else {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, R.string.positioning_robot_failed, Toast.LENGTH_LONG).show()
                    }
                }
            }
            true
        }
        lifecycleScope.launch {
                    val result = repository.getMapImage(projectID, map?.map_id ?: 0)

                    if (result is Result.Success) {
                        withContext(Dispatchers.Main){
                            iv?.let {
                                Glide.with(requireContext()).asBitmap().load(result.data.bytes()).into(it)
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, R.string.get_map_filed, Toast.LENGTH_LONG).show()
                        }
                    }
                }
    }



}