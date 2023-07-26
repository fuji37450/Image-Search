package com.example.imagesearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagesearch.R
import com.example.imagesearch.ui.screens.GridScreen
import com.example.imagesearch.ui.screens.PhotoViewModel

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
                GridScreen(photoViewModel.searchText, photoViewModel.photoUiState)
            }
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
            photoViewModel.searchText = text
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
            Row(
                modifier = Modifier
                    .clickable {
                        text = ""
                        active = false
                        photoViewModel.searchText = it
                        photoViewModel.getPhotos(it)
                        histories.remove(it)
                        histories.add(0, it)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .size(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f),
                    imageVector = Icons.Filled.History,
                    contentDescription = stringResource(R.string.history_icon)
                )
                Text(modifier = Modifier.weight(8f), text = it)
                Icon(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    imageVector = Icons.Filled.NorthWest,
                    contentDescription = stringResource(R.string.history_icon)
                )
            }
        }
    }
}