package com.example.imagesearch.data

import com.example.imagesearch.model.Photo
import com.example.imagesearch.network.PhotoApiService

interface PhotosRepository {
    suspend fun getPhotos(searchString: String): List<Photo>
}

class DefaultPhotosRepository(
    private val photoApiService: PhotoApiService
) : PhotosRepository {
    override suspend fun getPhotos(searchString: String): List<Photo> =
        photoApiService.getPhotos(searchString = searchString).hits
}