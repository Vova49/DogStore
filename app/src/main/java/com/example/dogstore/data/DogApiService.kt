package com.example.dogstore.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface DogInfoApi {
    @GET("cats?tags=cute")
    suspend fun getCatsList(): List<DogInfo>
}

interface DogImageApi {
    @GET
    suspend fun getDogImage(@Url url: String): okhttp3.ResponseBody
}

object DogApiService {
    private const val DOG_INFO_BASE_URL = "https://cataas.com/api/"
    private const val DOG_IMAGE_BASE_URL = "https://images.dog.ceo/"
    
    private val dogInfoRetrofit = Retrofit.Builder()
        .baseUrl(DOG_INFO_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val dogImageRetrofit = Retrofit.Builder()
        .baseUrl(DOG_IMAGE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val dogInfoApi: DogInfoApi = dogInfoRetrofit.create(DogInfoApi::class.java)
    val dogImageApi: DogImageApi = dogImageRetrofit.create(DogImageApi::class.java)
    
    // Реальные API методы
    suspend fun getDogInfoList(): List<DogInfo> {
        return try {
            dogInfoApi.getCatsList()
        } catch (e: Exception) {
            // Возвращаем моковые данные в случае ошибки
            listOf(
                DogInfo("1", "2023-01-01", listOf("friendly", "playful")),
                DogInfo("2", "2023-02-15", listOf("calm", "smart")),
                DogInfo("3", "2023-03-20", listOf("energetic", "loyal")),
                DogInfo("4", "2023-04-10", listOf("protective", "gentle"))
            )
        }
    }
    
    suspend fun getDogImageUrl(id: String): okhttp3.ResponseBody? {
        return try {
            dogImageApi.getDogImage("https://images.dog.ceo/breeds/pitbull/pitbull_dog.jpg")
        } catch (e: Exception) {
            null
        }
    }
}