package com.example.recyclerviewapi.service

import com.example.recyclerviewapi.model.RandomUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserService {

    @GET("/api")
    fun getRandomUser(@Query("results") quantity: Int): Call<RandomUser>
}