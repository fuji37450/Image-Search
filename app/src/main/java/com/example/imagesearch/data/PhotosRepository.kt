package com.example.imagesearch.data

import com.example.imagesearch.model.Photo
import com.example.imagesearch.network.PhotoApiService

interface PhotosRepository {
    suspend fun getPhotos(): List<Photo>
}

class DefaultPhotosRepository(private val photoApiService : PhotoApiService) : PhotosRepository {
    override suspend fun getPhotos(): List<Photo>  = photoApiService.getPhotos()
}