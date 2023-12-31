package com.example.imagesearch.network

import com.example.imagesearch.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface PhotoApiService {
    @GET("api")
    suspend fun getPhotos(
        @Query("key") apiKey: String = "",
        @Query("q") searchString: String,
    ): PhotoResponse
}