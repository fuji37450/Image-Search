package com.example.imagesearch.data

import com.example.imagesearch.network.PhotoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val photosRepository: PhotosRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://pixabay.com/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: PhotoApiService by lazy {
        retrofit.create(PhotoApiService::class.java)
    }

    override val photosRepository: PhotosRepository by lazy {
        DefaultPhotosRepository("fuji", retrofitService)
    }
}