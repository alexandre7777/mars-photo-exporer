package com.example.marsrover.service.model

import com.google.gson.annotations.SerializedName

data class RoverRemoteModel(
    val id: Int,
    @field:SerializedName("landing_date") val landingDate: String,
    @field:SerializedName("launch_date") val launchDate: String,
    val name: String,
    val status: String
)