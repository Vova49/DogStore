package com.example.dogstore.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    private val dogApiService = DogApiService
    
    // Получение списка информации о животных из API
    suspend fun getDogsList(): List<DogInfo> = withContext(Dispatchers.IO) {
        return@withContext dogApiService.getDogInfoList()
    }
    
    // Получение изображения животного по ID
    suspend fun getDogImage(id: String): ByteArray? = withContext(Dispatchers.IO) {
        val response = dogApiService.getDogImageUrl(id)
        return@withContext response?.bytes()
    }
}