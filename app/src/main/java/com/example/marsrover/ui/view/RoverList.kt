package com.example.marsrover.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marsrover.R
import com.example.marsrover.domain.model.roverUiModelList

@Composable
fun RoverList(
    modifier: Modifier,
    onClick: (roverName: String) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = modifier.fillMaxSize()) {
        LazyColumn {
            items(count = roverUiModelList.size, itemContent = { index ->
                Rover(
                    name = roverUiModelList[index].name,
                    img = roverUiModelList[index].img,
                    landingDate = roverUiModelList[index].landingDate,
                    distanceTraveled = roverUiModelList[index].distance,
                    onClick = onClick
                )
            })
        }
    }
}

@Preview
@Composable
fun RoverPreview() {
    Rover("Perseverance", R.drawable.perseverance, "18 February 2021", "12.56 km") {}
}

@Composable
fun Rover(
    name: String,
    img: Int,
    landingDate: String,
    distanceTraveled: String,
    onClick: (roverName: String) -> Unit
) {
    Card(modifier = Modifier
        .padding(16.dp)
        .clickable { onClick(name) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Image(painter = painterResource(id = img), contentDescription = null)
            Text(text = "Credit: NASA/JPL", fontSize = 8.sp)
            Text(text = "Landing date: $landingDate", fontSize = 12.sp)
            Text(text = "Distance traveled: $distanceTraveled", fontSize = 12.sp)
        }
    }
}