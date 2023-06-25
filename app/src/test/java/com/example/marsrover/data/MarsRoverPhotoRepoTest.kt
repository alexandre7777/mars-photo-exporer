package com.example.marsrover.data

import com.example.marsrover.MainDispatcherRule
import com.example.marsrover.db.MarsRoverSavedLocalModel
import com.example.marsrover.db.MarsRoverSavedPhotoDao
import com.example.marsrover.domain.model.RoverPhotoUiModel
import com.example.marsrover.domain.model.RoverPhotoUiState
import com.example.marsrover.service.MarsRoverPhotoService
import com.example.marsrover.service.model.CameraRemoteModel
import com.example.marsrover.service.model.PhotoRemoteModel
import com.example.marsrover.service.model.RoverPhotoRemoteModel
import com.example.marsrover.service.model.RoverRemoteModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class MarsRoverPhotoRepoTest {

    private val marsRoverPhotoService = mockkClass(MarsRoverPhotoService::class)
    private val marsRoverSavedPhotoDao = mockkClass(MarsRoverSavedPhotoDao::class)

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Test
    fun `should emit success when service and dao return valid data`() =
        runTest(coroutineRule.testDispatcher) {
            //Given
            val roverPhotoRemoteModel = RoverPhotoRemoteModel(
                photos = listOf(
                    PhotoRemoteModel(
                        camera = CameraRemoteModel(
                            fullName = "Camera One",
                            id = 1,
                            name = "Camera 1",
                            roverId = 1
                        ),
                        earthDate = "2022-07-02",
                        id = 2,
                        imgSrc = "https://example.com/photo1",
                        rover = RoverRemoteModel(
                            id = 5,
                            landingDate = "2021-02-18",
                            launchDate = "2020-07-30",
                            name = "Perseverance",
                            status = "active"
                        ),
                        sol = 20
                    ),
                    PhotoRemoteModel(
                        camera = CameraRemoteModel(
                            fullName = "Camera Two",
                            id = 3,
                            name = "Camera 2",
                            roverId = 1
                        ),
                        earthDate = "2022-07-02",
                        id = 4,
                        imgSrc = "https://example.com/photo2",
                        rover = RoverRemoteModel(
                            id = 5,
                            landingDate = "2021-02-18",
                            launchDate = "2020-07-30",
                            name = "Perseverance",
                            status = "active"
                        ),
                        sol = 20
                    )
                )
            )
            coEvery {
                marsRoverPhotoService.getMarsRoverPhotos("perseverance", "0")
            } returns roverPhotoRemoteModel
            coEvery {
                marsRoverSavedPhotoDao.allSavedIds("0", "perseverance")
            } returns flowOf(listOf(2))

            //When
            val marsRoverPhotoRepo =
                MarsRoverPhotoRepo(marsRoverPhotoService, marsRoverSavedPhotoDao)
            val result = marsRoverPhotoRepo.getMarsRoverPhoto("perseverance", "0").toList()

            //Then
            val expectedResult = RoverPhotoUiState.Success(
                roverPhotoUiModelList = listOf(
                    RoverPhotoUiModel(
                        id = 2,
                        roverName = "Perseverance",
                        imgSrc = "https://example.com/photo1",
                        sol = "20",
                        earthDate = "2022-07-02",
                        cameraFullName = "Camera One",
                        isSaved = true
                    ),
                    RoverPhotoUiModel(
                        id = 4,
                        roverName = "Perseverance",
                        imgSrc = "https://example.com/photo2",
                        sol = "20",
                        earthDate = "2022-07-02",
                        cameraFullName = "Camera Two",
                        isSaved = false
                    )
                )
            )
            assertEquals(1, result.size)
            assertEquals(expectedResult, result[0])
        }

    @Test
    fun `should emit error when service throw exception`() = runTest(coroutineRule.testDispatcher) {
        //Given
        coEvery {
            marsRoverPhotoService.getMarsRoverPhotos("perseverance", "0")
        } throws TimeoutException()
        coEvery {
            marsRoverSavedPhotoDao.allSavedIds("0", "perseverance")
        } returns flowOf(listOf(2))

        //When
        val marsRoverPhotoRepo = MarsRoverPhotoRepo(marsRoverPhotoService, marsRoverSavedPhotoDao)
        val result = marsRoverPhotoRepo.getMarsRoverPhoto("perseverance", "0").toList()

        //Then
        assertEquals(1, result.size)
        assertEquals(RoverPhotoUiState.Error, result[0])
    }

    @Test
    fun `should call insert when saved photo is called`() = runTest(coroutineRule.testDispatcher) {
        //Given
        val roverPhotoUiModel = RoverPhotoUiModel(
            id = 2,
            roverName = "Perseverance",
            imgSrc = "https://example.com/photo1",
            sol = "20",
            earthDate = "2022-07-02",
            cameraFullName = "Camera One",
            isSaved = true
        )
        val marsRoverSavedLocalModel = MarsRoverSavedLocalModel(
            roverPhotoId = 2,
            roverName = "Perseverance",
            imgSrc = "https://example.com/photo1",
            sol = "20",
            earthDate = "2022-07-02",
            cameraFullName = "Camera One"
        )
        coEvery { marsRoverSavedPhotoDao.insert(marsRoverSavedLocalModel) } returns Unit

        //When
        val marsRoverPhotoRepo = MarsRoverPhotoRepo(marsRoverPhotoService, marsRoverSavedPhotoDao)
        marsRoverPhotoRepo.savePhoto(roverPhotoUiModel)

        //Then
        coVerify { marsRoverSavedPhotoDao.insert(marsRoverSavedLocalModel) }
    }

    @Test
    fun `should call delete when remove photo is called`() = runTest(coroutineRule.testDispatcher) {
        //Given
        val roverPhotoUiModel = RoverPhotoUiModel(
            id = 2,
            roverName = "Perseverance",
            imgSrc = "https://example.com/photo1",
            sol = "20",
            earthDate = "2022-07-02",
            cameraFullName = "Camera One",
            isSaved = true
        )
        val marsRoverSavedLocalModel = MarsRoverSavedLocalModel(
            roverPhotoId = 2,
            roverName = "Perseverance",
            imgSrc = "https://example.com/photo1",
            sol = "20",
            earthDate = "2022-07-02",
            cameraFullName = "Camera One"
        )
        coEvery { marsRoverSavedPhotoDao.delete(marsRoverSavedLocalModel) } returns Unit

        //When
        val marsRoverPhotoRepo = MarsRoverPhotoRepo(marsRoverPhotoService, marsRoverSavedPhotoDao)
        marsRoverPhotoRepo.removePhoto(roverPhotoUiModel)

        //Then
        coVerify { marsRoverSavedPhotoDao.delete(marsRoverSavedLocalModel) }
    }

    @Test
    fun `should emit ui model when all data are retrieved`() =
        runTest(coroutineRule.testDispatcher) {
            //Given
            val marsRoverSavedLocalModelList = listOf(
                MarsRoverSavedLocalModel(
                    roverPhotoId = 2,
                    roverName = "Perseverance",
                    imgSrc = "https://example.com/photo1",
                    sol = "20",
                    earthDate = "2022-07-02",
                    cameraFullName = "Camera One"
                ),
                MarsRoverSavedLocalModel(
                    roverPhotoId = 4,
                    roverName = "Perseverance",
                    imgSrc = "https://example.com/photo2",
                    sol = "20",
                    earthDate = "2022-07-02",
                    cameraFullName = "Camera Two"
                )
            )
            coEvery { marsRoverSavedPhotoDao.getAllSaved() } returns flowOf(
                marsRoverSavedLocalModelList
            )

            //When
            val marsRoverPhotoRepo =
                MarsRoverPhotoRepo(marsRoverPhotoService, marsRoverSavedPhotoDao)
            val result = marsRoverPhotoRepo.getAllSaved().first()

            //Then
            val expectedResult = RoverPhotoUiState.Success(
                roverPhotoUiModelList = listOf(
                    RoverPhotoUiModel(
                        id = 2,
                        roverName = "Perseverance",
                        imgSrc = "https://example.com/photo1",
                        sol = "20",
                        earthDate = "2022-07-02",
                        cameraFullName = "Camera One",
                        isSaved = true
                    ),
                    RoverPhotoUiModel(
                        id = 4,
                        roverName = "Perseverance",
                        imgSrc = "https://example.com/photo2",
                        sol = "20",
                        earthDate = "2022-07-02",
                        cameraFullName = "Camera Two",
                        isSaved = true
                    )
                )
            )
            assertEquals(expectedResult, result)
        }
}