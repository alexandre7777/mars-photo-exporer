package com.example.marsrover.ui.photolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsrover.data.MarsRoverPhotoRepo
import com.example.marsrover.di.IoDispatcher
import com.example.marsrover.domain.model.RoverPhotoUiModel
import com.example.marsrover.domain.model.RoverPhotoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarsRoverPhotoViewModel @Inject constructor(
    private val marsRoverPhotoRepo: MarsRoverPhotoRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _roverPhotoUiState: MutableStateFlow<RoverPhotoUiState> =
        MutableStateFlow(RoverPhotoUiState.Loading)

    val roverPhotoUiState: StateFlow<RoverPhotoUiState>
        get() = _roverPhotoUiState

    fun getMarsRoverPhoto(roverName: String, sol: String) {
        viewModelScope.launch(ioDispatcher) {
            _roverPhotoUiState.value = RoverPhotoUiState.Loading
            marsRoverPhotoRepo.getMarsRoverPhoto(roverName, sol).collect {
                _roverPhotoUiState.value = it
            }
        }
    }

    fun changeSaveStatus(roverPhotoUiModel: RoverPhotoUiModel) {
        viewModelScope.launch(ioDispatcher) {
            if (roverPhotoUiModel.isSaved) {
                marsRoverPhotoRepo.removePhoto(roverPhotoUiModel)
            } else {
                marsRoverPhotoRepo.savePhoto(roverPhotoUiModel = roverPhotoUiModel)
            }
        }
    }
}