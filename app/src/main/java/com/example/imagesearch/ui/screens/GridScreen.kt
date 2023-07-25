package com.example.imagesearch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.imagesearch.R
import com.example.imagesearch.model.Photo


@Composable
fun GridScreen(
    photoUiState: PhotoUiState, modifier: Modifier = Modifier
) {
    when (photoUiState) {
        is PhotoUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PhotoUiState.Success -> SearchResultScreen(
            photoUiState.photos,
            modifier = modifier.fillMaxWidth()
        )

        is PhotoUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun SearchResultScreen(photos: List<Photo>, modifier: Modifier) {
    Column {
        var isGridMode by remember { mutableStateOf(true) }
        ResultInfo(
            isGridMode,
            onCheckedChange = { modeState -> isGridMode = modeState },
            resultCount = photos.count(),
            searchText = "temp"
        )
        if (isGridMode) {
            GridResult(photos = photos, modifier)
        } else {
            ListResult(photos = photos, modifier)
        }
    }
}

@Composable
fun GridResult(photos: List<Photo>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(items = photos, key = { photo -> photo.id }) { photo ->
            PhotoCard(
                photo = photo,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun ListResult(photos: List<Photo>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(photos, key = { photo -> photo.id }) { photo ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .size(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(photo.previewURL)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.ic_broken_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = photo.tags,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.aspectRatio(16f / 9f)
                )
                Text(text = photo.tags)
            }
            Divider()
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = ""
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "失敗", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PhotoCard(photo: Photo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(photo.previewURL)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = photo.tags,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ResultInfo(
    isGridMode: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    resultCount: Int,
    searchText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(buildAnnotatedString {
            append("Found ")
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("$resultCount")
            }
            append(" result for ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(searchText)
            }
            append(":")
        })
        IconToggleButton(
            checked = isGridMode,
            onCheckedChange = { modeState -> onCheckedChange(modeState) },
        ) {
            Icon(
                imageVector = if (isGridMode) Icons.Outlined.ViewList else Icons.Outlined.GridView,
                contentDescription = stringResource(R.string.list_icon)
            )
        }
    }
}