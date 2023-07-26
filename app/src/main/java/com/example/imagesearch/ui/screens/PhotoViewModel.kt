package com.example.imagesearch.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.imagesearch.PhotosApplication
import com.example.imagesearch.data.PhotosRepository
import com.example.imagesearch.model.Photo
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface PhotoUiState {
    object Init : PhotoUiState
    object Empty : PhotoUiState
    data class Success(val photos: List<Photo>) : PhotoUiState
    object Error : PhotoUiState
    object Loading : PhotoUiState
}

class PhotoViewModel(private val photosRepository: PhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var photoUiState: PhotoUiState by mutableStateOf(PhotoUiState.Loading)
        private set

    init {
        photoUiState = PhotoUiState.Init
    }

    /**
     * Gets photos information from the pixabay API Retrofit service and updates the
     * [Photo] [List] [MutableList].
     */
    fun getPhotos(searchString: String) {
        viewModelScope.launch {
            photoUiState = PhotoUiState.Loading
            photoUiState = try {
                val photos = photosRepository.getPhotos(searchString)
                if (photos.isEmpty()) {
                    PhotoUiState.Empty
                } else {
                    PhotoUiState.Success(photos)
                }
            } catch (e: IOException) {
                PhotoUiState.Error
            } catch (e: HttpException) {
                PhotoUiState.Error
            }
        }
    }

    /**
     * Factory for [PhotoViewModel] that takes [PhotosRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PhotosApplication)
                val photosRepository = application.container.photosRepository
                PhotoViewModel(photosRepository = photosRepository)
            }
        }
    }
}