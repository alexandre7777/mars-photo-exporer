package com.example.marsrover.data

import com.example.marsrover.db.MarsRoverSavedPhotoDao
import com.example.marsrover.domain.model.RoverPhotoUiModel
import com.example.marsrover.domain.model.RoverPhotoUiState
import com.example.marsrover.domain.model.toDbModel
import com.example.marsrover.domain.model.toUiModel
import com.example.marsrover.service.MarsRoverPhotoService
import com.example.marsrover.service.model.RoverPhotoRemoteModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarsRoverPhotoRepo @Inject constructor(
    private val marsRoverPhotoService: MarsRoverPhotoService,
    private val marsRoverSavedPhotoDao: MarsRoverSavedPhotoDao
) {

    private fun getAllRemoteMarsRoverPhotos(
        roverName: String,
        sol: String
    ): Flow<RoverPhotoRemoteModel?> = flow {
        try {
            val networkResult = marsRoverPhotoService.getMarsRoverPhotos(
                roverName.lowercase(),
                sol
            )
            emit(networkResult)
        } catch (throwable: Throwable) {
            emit(null)
        }
    }

    fun getMarsRoverPhoto(roverName: String, sol: String) : Flow<RoverPhotoUiState> =
        marsRoverSavedPhotoDao.allSavedIds(sol, roverName).combine(
            getAllRemoteMarsRoverPhotos(roverName, sol)
        ) { local, remote ->
            remote?.let { roverPhotoRemoteModel ->
                RoverPhotoUiState.Success(
                    roverPhotoRemoteModel.photos.map { photo ->
                        RoverPhotoUiModel(
                            id = photo.id,
                            roverName = photo.rover.name,
                            imgSrc = photo.imgSrc,
                            sol = photo.sol.toString(),
                            earthDate = photo.earthDate,
                            cameraFullName = photo.camera.fullName,
                            isSaved = local.contains(photo.id)
                        )
                    }
                )
            } ?: run {
                RoverPhotoUiState.Error
            }
        }

    suspend fun savePhoto(roverPhotoUiModel: RoverPhotoUiModel) {
        marsRoverSavedPhotoDao.insert(toDbModel(roverPhotoUiModel))
    }

    suspend fun removePhoto(roverPhotoUiModel: RoverPhotoUiModel) {
        marsRoverSavedPhotoDao.delete(toDbModel(roverPhotoUiModel))
    }

    fun getAllSaved(): Flow<RoverPhotoUiState> =
        marsRoverSavedPhotoDao.getAllSaved().map { localModel ->
            RoverPhotoUiState.Success(toUiModel(localModel))
        }
}