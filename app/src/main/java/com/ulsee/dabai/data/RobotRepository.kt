package com.ulsee.dabai.data

import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.ulsee.dabai.data.request.RobotVelocity
import com.ulsee.dabai.data.response.MapInfo
import com.ulsee.dabai.data.response.RobotStatus
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class RobotRepository {

    val TAG = "RobotRepository"
    private var mSocket: Socket? = null
    private var mEventListener: EventListener? = null

    init {
        connect()
    }

    interface EventListener {
//        fun onStatus(status: RobotStatus)
        fun onPositionUpdated(pose: Array<Double>)
        fun onMapUpdated(data: MapInfo)
        fun onConnected()
        fun onConnectFailed(exception: Exception)
        fun onError(exception: Exception)
    }

    fun setEventListener(listener: EventListener) {
        mEventListener = listener
    }

    private fun connect() {
        val uri = "http://192.168.10.5:9980"
        val opts = IO.Options()
        opts.transports = arrayOf("websocket")
        mSocket = IO.socket(uri, opts)
        mSocket?.let {
            it.on("RobotStatus") { data ->
                val jsonStr = data[0].toString()
                val robotStatus = Gson().fromJson(jsonStr, RobotStatus::class.java)
                Log.d(TAG, "on RobotStatus:"+robotStatus.motion.pose[0]+","+robotStatus.motion.pose[1])
                mEventListener?.onPositionUpdated(robotStatus.motion.pose)
            }

            it.on("MapUpdate") { data ->
                val jsonStr = data[0].toString()
                val data = Gson().fromJson(jsonStr, MapInfo::class.java)
                Log.d(TAG, "on MapInfo:"+data.width)
                mEventListener?.onMapUpdated(data)
            }
            it.connect()
                .on(Socket.EVENT_CONNECT) {
                    Log.d(TAG, "Socket connected!!!!!")
                    mEventListener?.onConnected()
                }
                .on(Socket.EVENT_CONNECT_ERROR) {
                    Log.d(TAG, "EVENT_CONNECT_ERROR!!!!!"+it[0].toString())
                    (it[0] as Exception).let { mEventListener?.onConnectFailed(it) }
                }
                .on(Socket.EVENT_ERROR) {
                    Log.d(TAG, "EVENT_ERROR!!!!!"+it[0].toString())
                    (it[0] as Exception).let { mEventListener?.onError(it) }
                }

        }
    }

    fun move(x: Double) { // [-1, 1]
        if (x < -1) throw IllegalArgumentException("move(x), x should be in range [-1, 1]")
        if (x > 1) throw IllegalArgumentException("move(x), x should be in range [-1, 1]")
        setVelocity(RobotVelocity(arrayListOf(x, 0.0, 0.0, 0.0, 0.0, 0.0)))
        // arrayListOf(x, y, z, roll, pitch, yaw)
    }

    fun rotate(yaw: Double) { // [-100, 100]
        if (yaw < -100) throw IllegalArgumentException("rotate(yaw), yaw should be in range [-100, 100]")
        if (yaw > 100) throw IllegalArgumentException("rotate(yaw), yaw should be in range [-100, 100]")
        setVelocity(RobotVelocity(arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, yaw)))
        // arrayListOf(x, y, z, roll, pitch, yaw)
    }

    private fun setVelocity(data: RobotVelocity) {

        val jsonArray = JSONArray()
        jsonArray.put(data.velocity[0])
        jsonArray.put(data.velocity[1])
        jsonArray.put(data.velocity[2])
        jsonArray.put(data.velocity[3])
        jsonArray.put(data.velocity[4])
        jsonArray.put(data.velocity[5])
        val jsonObject = JSONObject()
        jsonObject.put("velocity", jsonArray)

        mSocket?.emit("send_velocity", jsonObject)

        Log.d(TAG, "setVelocity: "+jsonObject.toString())
    }
}