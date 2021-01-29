package com.ulsee.dabai.ui.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapListItemViewModel : ViewModel() {
    val name: LiveData<String> = MutableLiveData("name")
}