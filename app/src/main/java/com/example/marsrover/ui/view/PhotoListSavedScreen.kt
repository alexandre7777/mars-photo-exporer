package com.example.marsrover.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.marsrover.domain.model.RoverPhotoUiState
import com.example.marsrover.ui.savedlist.MarsRoverSavedViewModel

@Composable
fun PhotoListSavedScreen(
    modifier: Modifier = Modifier,
    marsRoverSavedViewModel: MarsRoverSavedViewModel
) {
    val viewState by marsRoverSavedViewModel.marsPhotoUiSavedState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        marsRoverSavedViewModel.getAllSaved()
    }

    when (val roverPhotoUiState = viewState) {
        RoverPhotoUiState.Error -> Error()
        RoverPhotoUiState.Loading -> Loading()
        is RoverPhotoUiState.Success -> PhotoList(
            modifier = modifier,
            roverPhotoUiModelList = roverPhotoUiState.roverPhotoUiModelList,
            onClick = { roverPhotoUiModel ->
                marsRoverSavedViewModel.changeSaveStatus(roverPhotoUiModel)
            }
        )
    }
}