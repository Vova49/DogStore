package com.example.dogstore.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    private val dogApiService = DogApiService
    private val loadingStateManager = LoadingStateManager()

    // Getting list of animals with state tracking
    suspend fun getDogsListWithLoading(): List<DogInfo> = withContext(Dispatchers.IO) {
        try {
            loadingStateManager.startLoading()
            val dogs = dogApiService.getDogInfoList()
            loadingStateManager.startLoading(totalItems = dogs.size)
            loadingStateManager.updateItemsLoaded(dogs.size)
            return@withContext dogs
        } catch (e: Exception) {
            loadingStateManager.setError(e.message ?: "Unknown error")
            return@withContext emptyList()
        }
    }
    
    // Getting animal image by ID (legacy method)
    suspend fun getDogImage(): ByteArray? = withContext(Dispatchers.IO) {
        val response = dogApiService.getDogImageUrl()
        return@withContext response?.bytes()
    }

}