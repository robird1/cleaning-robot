package com.ulsee.dabai.ui.map

data class CreateMapFormState(val mapNameError: Int? = null,
                          val mapFloorError: Int? = null,
                          val isDataValid: Boolean = false)