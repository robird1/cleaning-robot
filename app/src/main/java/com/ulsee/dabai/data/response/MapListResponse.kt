package com.ulsee.dabai.data.response

data class MapListResponse(
    val data: List<Map>
)
data class Map (
    val map_id: Int,
    val map_name: String,
    val map_url: String,
    val map_info: MapInfo,
    var uploadable: Boolean?,// calculated
    //
    val x_origin: Double,
    val y_origin: Double,
    val height: Int,
    val width: Int,
    val resolution: Double,
    val floor: Int,
)
data class MapInfo (
    val map_id: Int,
    val x_origin: Double,
    val y_origin: Double,
    val height: Int,
    val width: Int,
    val resolution: Double,
    val floor: Int,
    val map_name: String,
    var uploadable: Boolean?,// calculated
)