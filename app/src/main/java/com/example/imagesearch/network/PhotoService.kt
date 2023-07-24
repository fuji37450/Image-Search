package com.example.imagesearch.network

import com.example.imagesearch.model.Photo
import retrofit2.http.GET


interface PhotoApiService {
    @GET("photos")
    suspend fun getPhotos(): List<Photo>
}