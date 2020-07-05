package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.backend.DealsRepo
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem

class DealsListViewModel(private val repo: DealsRepo) : ViewModel() {
    val canvasUnit: LiveData<CanvasInfo>
        get() = repo.canvasInfo()

    val dealsList: LiveData<List<DealItem>>
        get() = repo.dealsList()
}