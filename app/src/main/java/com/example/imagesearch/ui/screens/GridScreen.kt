package com.example.imagesearch.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.imagesearch.R
import com.example.imagesearch.model.Photo


@Composable
fun GridScreen(
    searchText: String, photoUiState: PhotoUiState, modifier: Modifier = Modifier
) {
    when (photoUiState) {
        is PhotoUiState.Init -> InitScreen(modifier = modifier.fillMaxSize())
        is PhotoUiState.Empty -> EmptyScreen(
            searchText,
            modifier = modifier.fillMaxSize()
        )

        is PhotoUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is PhotoUiState.Success -> SearchResultScreen(
            searchText,
            photoUiState.photos,
            modifier = modifier.fillMaxWidth()
        )

        is PhotoUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun SearchResultScreen(searchText: String, photos: List<Photo>, modifier: Modifier) {
    Column {
        var isGridMode by remember { mutableStateOf(true) }
        ResultInfo(
            isGridMode,
            onCheckedChange = { modeState -> isGridMode = modeState },
            searchText = searchText
        )
        if (isGridMode) {
            GridResult(photos = photos, modifier)
        } else {
            ListResult(photos = photos, modifier)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridResult(photos: List<Photo>, modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxWidth(),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(photos, key = { photo -> photo.id }) { photo ->
                PhotoCard(
                    photo = photo, modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    )
}

@Composable
fun ListResult(photos: List<Photo>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(photos, key = { _, photo -> photo.id }) { index, photo ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .size(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(photo.previewURL)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.broken_img),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = photo.tags,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .aspectRatio(16f / 9f)
                )
                Text(modifier = modifier.padding(4.dp), text = photo.tags)
            }
            if (index < photos.lastIndex) Divider()
        }
    }
}

@Composable
fun InitScreen(modifier: Modifier) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ImageSearch,
            contentDescription = stringResource(R.string.image_search_icon),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(text = "Type something to search")
    }
}

@Composable
fun EmptyScreen(searchText: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(buildAnnotatedString {
            append("No result found for ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(searchText)
            }
        })
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.connection_error_img), contentDescription = ""
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
                error = painterResource(R.drawable.broken_img),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = photo.tags,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Text(modifier = modifier.padding(12.dp), text = photo.tags)
        }
    }
}

@Composable
fun ResultInfo(
    isGridMode: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    searchText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(buildAnnotatedString {
            append("Results for ")
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