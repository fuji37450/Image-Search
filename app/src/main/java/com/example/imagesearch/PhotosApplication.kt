package com.example.imagesearch

import android.app.Application
import com.example.imagesearch.data.AppContainer
import com.example.imagesearch.data.DefaultAppContainer

class PhotosApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
