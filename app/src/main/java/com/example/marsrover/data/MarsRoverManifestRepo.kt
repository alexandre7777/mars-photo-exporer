package com.example.marsrover.data

import com.example.marsrover.domain.model.RoverManifestUiState
import com.example.marsrover.domain.model.toUiModel
import com.example.marsrover.service.MarsRoverManifestService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MarsRoverManifestRepo @Inject constructor(
    private val marsRoverManifestService: MarsRoverManifestService
) {
    fun getMarsRoverManifest(roverName: String): Flow<RoverManifestUiState> = flow {
        try {
            emit(
                toUiModel(
                    marsRoverManifestService.getMarsRoverManifest(
                        roverName.lowercase()
                    )
                )
            )
        } catch (throwable: Throwable) {
            emit(RoverManifestUiState.Error)
        }
    }
}