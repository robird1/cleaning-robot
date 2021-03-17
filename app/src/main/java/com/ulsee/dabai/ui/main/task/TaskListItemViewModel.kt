package com.ulsee.dabai.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListItemViewModel : ViewModel() {
    val name: LiveData<String> = MutableLiveData("name")
    private var mOnExecuteCallback : onExecuteCallback? = null

    interface onExecuteCallback {
        fun onExecute()
    }

    fun setOnExecuteCallback(cb: onExecuteCallback) {
        mOnExecuteCallback = cb
    }

    fun onExecute() {
        mOnExecuteCallback?.onExecute()
    }
}