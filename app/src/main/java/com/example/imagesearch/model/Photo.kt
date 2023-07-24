package com.example.imagesearch.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
//    val id: Int,
//    val tags: String,
//    @DrawableRes val imageResourceId: Int
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)
