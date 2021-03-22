package com.ulsee.dabai.ui.main.robot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RobotListItemViewModel : ViewModel() {
    val name: LiveData<String> = MutableLiveData("name")

    // position
    private var mOnPositionCallback : onPositionCallback? = null

    interface onPositionCallback {
        fun onPosition()
    }

    fun setOnPositionCallback(cb: onPositionCallback) {
        mOnPositionCallback = cb
    }

    fun onPosition() {
        mOnPositionCallback?.onPosition()
    }

    // show map list
    private var mOnMapCallback : onMapCallback? = null

    interface onMapCallback {
        fun onMap()
    }

    fun setOnMapCallback(cb: onMapCallback) {
        mOnMapCallback = cb
    }
    fun onMap() {
        mOnMapCallback?.onMap()
    }
}