package com.ulsee.dabai.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskListItemViewModel : ViewModel() {
    val name: LiveData<String> = MutableLiveData("name")
}