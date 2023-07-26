package com.example.imagesearch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
import com.example.imagesearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(photoViewModel: PhotoViewModel) {
    var inputText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val histories = remember { mutableStateListOf<String>() }
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = inputText,
        onQueryChange = {
            inputText = it.trim()
        },
        onSearch = {
            histories.add(0, inputText)
            if (histories.size > 10) {
                histories.removeLast()
            }
            photoViewModel.updateSearchText(inputText)
            photoViewModel.getPhotos(inputText)
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
            if (active) {
                Icon(
                    modifier = Modifier.clickable { active = false },
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        },
        trailingIcon = {
            if (inputText.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        if (inputText.isNotEmpty()) {
                            inputText = ""
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
                        inputText = it
                        active = false
                        photoViewModel.updateSearchText(it)
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
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Filled.History,
                    contentDescription = stringResource(R.string.history_icon)
                )
                Text(text = it)
            }
        }
    }
}