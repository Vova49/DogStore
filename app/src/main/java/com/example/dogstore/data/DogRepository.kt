package com.example.dogstore.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class DogRepository {
    private val dogApiService = DogApiService
    private val loadingStateManager = LoadingStateManager()
    private val imageCache = mutableMapOf<String, ByteArray?>()

    // Getting list of animals with state tracking and image preloading
    suspend fun getDogsListWithLoading(): List<DogInfo> = withContext(Dispatchers.IO) {
        try {
            loadingStateManager.startLoading()
            val dogs = dogApiService.getDogInfoList()
            
            // Preload all images during loading indicator display
            loadingStateManager.startLoading(totalItems = dogs.size)
            preloadAllImages(dogs)
            
            loadingStateManager.updateItemsLoaded(dogs.size)
            return@withContext dogs
        } catch (e: Exception) {
            loadingStateManager.setError(e.message ?: "Unknown error")
            return@withContext emptyList()
        }
    }
    
    // Preload all images for the dogs list
    private suspend fun preloadAllImages(dogs: List<DogInfo>) = withContext(Dispatchers.IO) {
        val imageJobs = dogs.map { dog ->
            async {
                try {
                    val response = dogApiService.getDogImageUrl()
                    val imageBytes = response?.bytes()
                    imageCache[dog.id] = imageBytes
                } catch (e: Exception) {
                    imageCache[dog.id] = null
                }
            }
        }
        imageJobs.awaitAll()
    }
    
    // Get preloaded image for a specific dog
    fun getPreloadedDogImage(dogId: String): ByteArray? {
        return imageCache[dogId]
    }
}