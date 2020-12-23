package com.example.recyclerviewapi.repository

import android.util.Log
import com.example.recyclerviewapi.model.RandomUser
import com.example.recyclerviewapi.service.RandomUserService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RandomUserRepository {
    private val randomUserService: RandomUserService

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        randomUserService = retrofit.create(RandomUserService::class.java)
    }

    fun getRandomUsers(quantity: Int = 1): RandomUser?{
        var randomUser: RandomUser? = null
        if(quantity >= 1){
            val response  = randomUserService.getRandomUser(quantity)
            randomUser = response.execute().body()
        }else{
            Log.e(RandomUserRepository::class.qualifiedName, "[ERROR] The quantity must be greater than 0.")
        }
        return randomUser
    }
}