package com.example.imagesearch.network

import com.example.imagesearch.model.PhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface PhotoApiService {
    @GET("api")
    suspend fun getPhotos(
        @Query("key") apiKey: String = "38433874-7c8291405628f5642ed3a07ab",
        @Query("q") searchString: String,
    ): PhotoResponse
}