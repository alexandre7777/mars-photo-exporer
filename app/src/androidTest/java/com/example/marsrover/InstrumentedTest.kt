package com.example.marsrover

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marsrover.domain.model.RoverManifestUiModel
import com.example.marsrover.domain.model.RoverPhotoUiModel
import com.example.marsrover.ui.theme.MarsRoverTheme
import com.example.marsrover.ui.view.ManifestList
import com.example.marsrover.ui.view.PhotoList
import com.example.marsrover.ui.view.RoverList

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRoverList() {
        //When
        composeTestRule.setContent {
            MarsRoverTheme {
                RoverList(modifier = Modifier) {}
            }
        }

        //Then
        composeTestRule.onNodeWithText("Perseverance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Curiosity").assertIsDisplayed()
        composeTestRule.onNodeWithText("Landing date: 18 February 2021").assertIsDisplayed()
        composeTestRule.onNodeWithText("Distance traveled: 12.56 km").assertIsDisplayed()
    }

    @Test
    fun testManifestList() {
        //Given
        val roverManifestUiModelList = listOf(
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

        //When
        composeTestRule.setContent {
            MarsRoverTheme {
                ManifestList(
                    modifier = Modifier,
                    roverManifestUiModelList = roverManifestUiModelList,
                    roverName = ""
                ) { _, _ -> }
            }
        }

        //Then
        composeTestRule.onNodeWithText("Sol: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Earth date: 2021-02-18").assertIsDisplayed()
        composeTestRule.onNodeWithText("Number of photo: 54").assertIsDisplayed()
        composeTestRule.onNodeWithText("Earth date: 2021-02-19").assertIsDisplayed()
        composeTestRule.onNodeWithText("Number of photo: 201").assertIsDisplayed()
    }

    @Test
    fun testPhotoList() {
        //Given
        val roverPhotoUiModelList = listOf(
            RoverPhotoUiModel(
                id = 1,
                roverName = "perseverance",
                imgSrc = "https://example.com/photo1",
                sol = "0",
                earthDate = "2022-03-10",
                cameraFullName = "Camera One",
                isSaved = true
            ),
            RoverPhotoUiModel(
                id = 2,
                roverName = "perseverance",
                imgSrc = "https://example.com/photo2",
                sol = "1",
                earthDate = "2022-03-11",
                cameraFullName = "Camera Two",
                isSaved = false
            )
        )

        //When
        composeTestRule.setContent {
            MarsRoverTheme {
                PhotoList(
                    modifier = Modifier,
                    roverPhotoUiModelList = roverPhotoUiModelList,
                ) { }
            }
        }

        //Then
        composeTestRule.onAllNodesWithContentDescription("save icon").assertCountEquals(2)
        composeTestRule.onNodeWithText("Sol: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Earth date: 2022-03-10").assertIsDisplayed()
        composeTestRule.onNodeWithText("Camera One").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sol: 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Earth date: 2022-03-11").assertIsDisplayed()
        composeTestRule.onNodeWithText("Camera Two").assertIsDisplayed()
    }
}