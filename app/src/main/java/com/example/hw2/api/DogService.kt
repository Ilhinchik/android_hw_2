package com.example.hw2.api

import retrofit2.Response
import retrofit2.http.GET

interface DogService {
    @GET("api/breeds/image/random")
    suspend fun getDogImage(): Response<DogImageResponse>
}

data class DogImageResponse(val message: String, val status: String)