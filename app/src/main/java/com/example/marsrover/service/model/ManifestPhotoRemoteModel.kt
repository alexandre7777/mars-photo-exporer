package com.example.marsrover.service.model

import com.google.gson.annotations.SerializedName

data class ManifestPhotoRemoteModel(
    val cameras: List<String>,
    @field:SerializedName("earth_date") val earthDate: String,
    val sol: Int,
    @field:SerializedName("total_photos") val totalPhotos: Int
)