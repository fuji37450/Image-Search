package com.example.imagesearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagesearch.ui.screens.PhotoViewModel
import com.example.imagesearch.ui.screens.SearchBox
import com.example.imagesearch.ui.screens.SearchResultScreen

@Composable
fun ImageSearchApp() {
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
                SearchBox(photoViewModel)
                SearchResultScreen(photoViewModel.searchText, photoViewModel.photoUiState)
            }
        }
    }
}