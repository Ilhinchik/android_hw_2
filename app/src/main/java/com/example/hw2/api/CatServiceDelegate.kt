package com.example.hw2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DogServiceDelegate {
    val URL = "https://dog.ceo/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: DogService by lazy {
        retrofit.create(DogService::class.java)
    }

    suspend fun getDogImageUrl(): String? {
        return api.getDogImage().body()?.message
    }
}