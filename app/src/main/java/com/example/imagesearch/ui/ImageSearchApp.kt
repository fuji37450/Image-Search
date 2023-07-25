package com.example.imagesearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagesearch.R
import com.example.imagesearch.ui.screens.GridScreen
import com.example.imagesearch.ui.screens.PhotoUiState
import com.example.imagesearch.ui.screens.PhotoViewModel
import com.example.imagesearch.ui.theme.ImageSearchTheme

@Composable
fun ImageResearchApp() {
    val photoViewModel: PhotoViewModel = viewModel(factory = PhotoViewModel.Factory)
    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 12.dp)
                    .fillMaxSize(),
            ) {
                SearchBox()
                ResultRow(photoUiState = photoViewModel.photoUiState)
                GridScreen(photoUiState = photoViewModel.photoUiState)
            }
        }
    }
}

@Composable
fun ResultRow(photoUiState: PhotoUiState) {
    when (photoUiState) {
        is PhotoUiState.Loading -> Text("Searching...")
        is PhotoUiState.Success -> ResultInfo(photoUiState.photos.count(), "tt")
        is PhotoUiState.Error -> Text("Some error occurred")
    }
}

@Composable
fun ResultInfo(resultCount: Int, searchText: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isGridMode by remember { mutableStateOf(true) }
        Text(buildAnnotatedString {
            append("Found ")
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("$resultCount")
            }
            append(" result for ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$searchText")
            }
            append(":")
        })
        IconToggleButton(
            checked = isGridMode,
            onCheckedChange = {
                isGridMode = !isGridMode
            },
        ) {
            Icon(
                imageVector = if (isGridMode) Icons.Outlined.GridView else Icons.Outlined.ViewList,
                contentDescription = stringResource(R.string.list_icon)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val histories = remember { mutableStateListOf<String>() }
    val photoViewModel: PhotoViewModel = viewModel(factory = PhotoViewModel.Factory)
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            histories.add(0, text)
            if (histories.size > 10) {
                histories.removeLast()
            }
            photoViewModel.getPhotos(text)
            text = ""
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = stringResource(R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_icon)
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.close_icon)
                )
            }
        }
    ) {
        histories.forEach {
            Row(modifier = Modifier) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Filled.History,
                    contentDescription = stringResource(R.string.history_icon)
                )
                Text(text = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    ImageSearchTheme {
        Column(Modifier.padding(vertical = 16.dp)) {
            SearchBox()
        }
    }
}