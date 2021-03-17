package com.ulsee.dabai.ui.robot_local

data class CreateMapFormState(val mapNameError: Int? = null,
                          val mapFloorError: Int? = null,
                          val isDataValid: Boolean = false)