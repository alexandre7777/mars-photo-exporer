package com.example.marsrover.domain.model

sealed class RoverPhotoUiState {
    data class Success(
        val roverPhotoUiModelList: List<RoverPhotoUiModel>
    ) : RoverPhotoUiState()
    object Loading : RoverPhotoUiState()
    object Error : RoverPhotoUiState()
}

data class RoverPhotoUiModel(
    val id: Int,
    val roverName: String,
    val imgSrc: String,
    val sol: String,
    val earthDate: String,
    val cameraFullName: String,
    val isSaved: Boolean
)