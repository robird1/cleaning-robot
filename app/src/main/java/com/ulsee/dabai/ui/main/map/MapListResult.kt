package com.ulsee.dabai.ui.main.map

import com.ulsee.dabai.data.response.Map

data class MapListResult (
    val success: List<Map>? = null,
    val error: Int? = null
)