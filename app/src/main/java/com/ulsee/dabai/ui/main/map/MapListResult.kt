package com.ulsee.dabai.ui.main.map

import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.data.response.MapInfo

data class MapListResult (
    val success: List<Map>? = null,
    val error: Int? = null
)
data class UploadMapResult (
        val success: Boolean,
        val error: Int? = null
)