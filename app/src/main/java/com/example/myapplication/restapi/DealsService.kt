package com.example.myapplication.restapi

import com.example.myapplication.restapi.models.ManagerSpecial
import com.example.myapplication.restapi.models.ManagerSpecialsResponse
import retrofit2.Call
import retrofit2.http.GET

interface DealsService {
    companion object {
        val baseUrl: String
            get() = "https://raw.githubusercontent.com/"
    }

    @GET("Swiftly-Systems/code-exercise-android/master/backup")
    fun getManagerSpecials(): Call<ManagerSpecialsResponse<ManagerSpecial>>
}