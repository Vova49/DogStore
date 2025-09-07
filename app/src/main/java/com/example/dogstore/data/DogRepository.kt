package com.example.dogstore.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    private val dogApiService = DogApiService
    private val loadingStateManager = LoadingStateManager()
    
    fun getLoadingStateManager(): LoadingStateManager = loadingStateManager
    
    // Getting list of animal information from API with state tracking
    suspend fun getDogsListWithLoading(): List<DogInfo> = withContext(Dispatchers.IO) {
        try {
            loadingStateManager.startLoading()
            val dogs = dogApiService.getDogInfoList()
            loadingStateManager.startLoading(totalItems = dogs.size, totalImages = dogs.size)
            loadingStateManager.updateItemsLoaded(dogs.size)
            return@withContext dogs
        } catch (e: Exception) {
            loadingStateManager.setError(e.message ?: "Unknown error")
            return@withContext emptyList()
        }
    }
    
    // Getting list of animal information from API (legacy method)
    suspend fun getDogsList(): List<DogInfo> = withContext(Dispatchers.IO) {
        return@withContext dogApiService.getDogInfoList()
    }
    
    // Getting animal image by ID with state tracking
    suspend fun getDogImageWithLoading(): ByteArray? = withContext(Dispatchers.IO) {
        try {
            loadingStateManager.setLoadingImages(true)
            val response = dogApiService.getDogImageUrl()
            val result = response?.bytes()
            loadingStateManager.updateImagesLoaded()
            loadingStateManager.setLoadingImages(false)
            return@withContext result
        } catch (e: Exception) {
            loadingStateManager.setLoadingImages(false)
            loadingStateManager.updateImagesLoaded()
            return@withContext null
        }
    }
    
    // Getting animal image by ID (legacy method)
    suspend fun getDogImage(): ByteArray? = withContext(Dispatchers.IO) {
        val response = dogApiService.getDogImageUrl()
        return@withContext response?.bytes()
    }
    
    // Checking if loading is complete
    fun isLoadingComplete(): Boolean {
        return loadingStateManager.isLoadingComplete()
    }
}