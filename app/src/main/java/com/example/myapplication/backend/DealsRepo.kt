package com.example.myapplication.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodels.DealItem
import com.example.myapplication.restapi.DealsService
import com.example.myapplication.restapi.models.ManagerSpecial
import com.example.myapplication.restapi.models.ManagerSpecialsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DealsRepo(private val dealsService: DealsService) {

    fun dealsList(): LiveData<List<DealItem>> {
        val liveData = MutableLiveData<List<DealItem>>()

        dealsService.getManagerSpecials()
            .enqueue(object : Callback<ManagerSpecialsResponse<ManagerSpecial>> {
                override fun onFailure(
                    call: Call<ManagerSpecialsResponse<ManagerSpecial>>,
                    t: Throwable
                ) {
                    call.cancel()
                }

                override fun onResponse(
                    call: Call<ManagerSpecialsResponse<ManagerSpecial>>,
                    response: Response<ManagerSpecialsResponse<ManagerSpecial>>
                ) {
                    val dealList = response.body()?.managerSpecials?.map {
                        DealItem(
                            dealImageUrl = it.imageUrl,
                            dealName = it.displayName
                        )
                    }

                    dealList?.let { liveData.postValue(it) }
                }

            })

        return liveData
    }
}