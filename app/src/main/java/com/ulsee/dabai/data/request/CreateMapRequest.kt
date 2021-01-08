package com.ulsee.dabai.data.request

data class CreateMapRequest (
    val map_name: String,
    val map_floor : Int,
    val cmd : Int
)