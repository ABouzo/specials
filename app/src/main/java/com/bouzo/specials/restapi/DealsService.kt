package com.bouzo.specials.restapi

import com.bouzo.specials.restapi.models.ManagerSpecial
import com.bouzo.specials.restapi.models.ManagerSpecialsResponse
import retrofit2.Call
import retrofit2.http.GET

interface DealsService {

    @GET("Swiftly-Systems/code-exercise-android/master/backup")
    fun getManagerSpecials(): Call<ManagerSpecialsResponse<ManagerSpecial>>
}