# Android App - Image Search

> An android application written in Kotlin.

## Features

- **Search Images**: The app allows users to search for images through
  the [Pixabay API](https://pixabay.com/api/docs/). It uses the Pixabay API to fetch image data
  based on user input.
- **Search Histories**: The app keeps track of search histories, allowing users to quickly access
  their previous search queries.
- **List and Grid View**: Users can toggle between list and grid views for search results.
- **MVVM Pattern**: The app follows the MVVM (Model-View-ViewModel) architectural pattern to
  maintain a clear separation of concerns and promote better code organization.
- **Jetpack Compose**: The app is built using Jetpack Compose, a modern toolkit for building native
  UI in Android.

## Getting Started

Include instructions on how to get the app up and running:

1. Clone the repository.
2. Open the project in Android Studio.
3. Add your Pixabay API key
   in [PhotoService.kt](app/src/main/java/com/example/imagesearch/network/PhotoService.kt)
   .
4. Build and run the app on your emulator or physical device.