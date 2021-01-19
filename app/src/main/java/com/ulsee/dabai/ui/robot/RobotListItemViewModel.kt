package com.ulsee.dabai.ui.robot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RobotListItemViewModel : ViewModel() {
    val name: LiveData<String> = MutableLiveData("name")
}