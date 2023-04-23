package com.example.marsrover.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marsrover.R
import com.example.marsrover.domain.model.RoverPhotoUiModel

@Composable
fun PhotoList(
    modifier: Modifier,
    roverPhotoUiModelList: List<RoverPhotoUiModel>,
    onClick: (roverPhotoUiModel: RoverPhotoUiModel) -> Unit
) {

    Surface(color = MaterialTheme.colorScheme.background, modifier = modifier.fillMaxSize()) {
        LazyColumn {
            items(count = roverPhotoUiModelList.size, itemContent = { index ->
                Photo(roverPhotoUiModel = roverPhotoUiModelList[index], onClick)
            })
        }
    }
}

@Composable
fun Photo(
    roverPhotoUiModel: RoverPhotoUiModel,
    onClick: (roverPhotoUiModel: RoverPhotoUiModel) -> Unit
) {
    Card(
        modifier = Modifier.padding(16.dp).clickable {
            onClick(roverPhotoUiModel)
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(
                        id = if (roverPhotoUiModel.isSaved) {
                            R.drawable.ic_save
                        } else {
                            R.drawable.ic_save_outline
                        }
                    ), contentDescription = "save icon"
                )

                Text(
                    text = roverPhotoUiModel.roverName,
                    modifier = Modifier.padding(16.dp)
                )
            }
            AsyncImage(
                model = roverPhotoUiModel.imgSrc,
                contentDescription = "rover photo",
                modifier = Modifier.height(300.dp)
            )

            Text(text = stringResource(id = R.string.sol, roverPhotoUiModel.sol))
            Text(text = stringResource(id = R.string.earth_date, roverPhotoUiModel.earthDate))
            Text(text = roverPhotoUiModel.cameraFullName)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PhotoPreview() {
    Photo(
        roverPhotoUiModel = RoverPhotoUiModel(
            id = 4,
            roverName = "Curiosity",
            imgSrc = "https://domain.com",
            sol = "34",
            earthDate = "2021-03-05",
            cameraFullName = "Mast Camera Zoom - Right",
            true
        )
    ) {}
}