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
    data class Success(val photos: List<Photo>) : PhotoUiState
    object Error : PhotoUiState
    object Loading : PhotoUiState
}

class PhotoViewModel(private val photosRepository: PhotosRepository) : ViewModel()  {
        /** The mutable State that stores the status of the most recent request */
        var photoUiState: PhotoUiState by mutableStateOf(PhotoUiState.Loading)
        private set

                init {
                    getPhotos()
                }

        /**
         * Gets Mars photos information from the Mars API Retrofit service and updates the
         * [Photo] [List] [MutableList].
         */
        fun getPhotos() {
            viewModelScope.launch {
                photoUiState = PhotoUiState.Loading
                photoUiState = try {
                    PhotoUiState.Success(photosRepository.getPhotos())
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