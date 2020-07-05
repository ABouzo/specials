package com.example.myapplication.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodels.DealItem

class DealsRepo {

    val dealsList: LiveData<List<DealItem>> = MutableLiveData(
        listOf(
            DealItem("someUrl", "someName"),
            DealItem("anotherUrl", "anotherName"),
            DealItem("thirdUrl", "thirdName")
        )
    )
}