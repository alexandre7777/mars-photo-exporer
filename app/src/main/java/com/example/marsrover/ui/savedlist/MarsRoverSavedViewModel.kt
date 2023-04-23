package com.example.marsrover.ui.savedlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsrover.data.MarsRoverPhotoRepo
import com.example.marsrover.domain.model.RoverPhotoUiModel
import com.example.marsrover.domain.model.RoverPhotoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarsRoverSavedViewModel @Inject constructor(
    private val marsRoverPhotoRepo: MarsRoverPhotoRepo
) : ViewModel() {

    private val _marsPhotoUiSavedState: MutableStateFlow<RoverPhotoUiState> =
        MutableStateFlow(RoverPhotoUiState.Loading)

    val marsPhotoUiSavedState: StateFlow<RoverPhotoUiState>
        get() = _marsPhotoUiSavedState

    fun getAllSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            marsRoverPhotoRepo.getAllSaved().collect {
                _marsPhotoUiSavedState.value = it
            }
        }
    }

    fun changeSaveStatus(roverPhotoUiModel: RoverPhotoUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (roverPhotoUiModel.isSaved) {
                marsRoverPhotoRepo.removePhoto(roverPhotoUiModel)
            } else {
                marsRoverPhotoRepo.savePhoto(roverPhotoUiModel = roverPhotoUiModel)
            }
        }
    }

}