package com.example.marsrover.ui.manifestlist

import com.example.marsrover.MainDispatcherRule
import com.example.marsrover.data.MarsRoverManifestRepo
import com.example.marsrover.domain.model.RoverManifestUiModel
import com.example.marsrover.domain.model.RoverManifestUiState
import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MarsRoverManifestViewModelTest {

    private val marsRoverManifestRepo = mockkClass(MarsRoverManifestRepo::class)

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Test
    fun `should emit error when repo return error`() = runTest(coroutineRule.testDispatcher) {
        //Given
        coEvery {
            marsRoverManifestRepo.getMarsRoverManifest("perseverance")
        } returns flowOf(RoverManifestUiState.Error)

        //When
        val marsRoverManifestViewModel = MarsRoverManifestViewModel(marsRoverManifestRepo, coroutineRule.testDispatcher)
        marsRoverManifestViewModel.getMarsRoverManifest("perseverance")
        val result = marsRoverManifestViewModel.roverManifestUiState.first()

        //Then
        assertEquals(RoverManifestUiState.Error, result)
    }

    @Test
    fun `should emit success when repo return success`() = runTest(coroutineRule.testDispatcher) {
        //Given
        val expectedResult = RoverManifestUiState.Success(
            listOf(
                RoverManifestUiModel(
                    sol = "1",
                    earthDate = "2021-02-19",
                    photoNumber = "201"
                ),
                RoverManifestUiModel(
                    sol = "0",
                    earthDate = "2021-02-18",
                    photoNumber = "54"
                )
            )
        )
        coEvery {
            marsRoverManifestRepo.getMarsRoverManifest("perseverance")
        } returns flowOf(expectedResult)

        //When
        val marsRoverManifestViewModel = MarsRoverManifestViewModel(marsRoverManifestRepo, coroutineRule.testDispatcher)
        marsRoverManifestViewModel.getMarsRoverManifest("perseverance")
        val result = marsRoverManifestViewModel.roverManifestUiState.first()

        //Then
        assertEquals(expectedResult, result)
    }
}