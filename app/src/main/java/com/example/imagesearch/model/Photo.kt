package com.example.imagesearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<Photo>,
)

@Serializable
data class Photo(
    val id: Int,
    val pageURL: String,
    val type: String,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    @SerialName(value = "webformatURL")
    val webFormatURL: String,
    @SerialName(value = "webformatWidth")
    val webFormatWidth: Int,
    @SerialName(value = "webformatHeight")
    val webFormatHeight: Int,
    val largeImageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val views: Int,
    val downloads: Int,
    val collections: Int,
    val likes: Int,
    val comments: Int,
    @SerialName(value = "user_id")
    val userId: Int,
    val user: String,
    val userImageURL: String
)
