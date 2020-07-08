package com.bouzo.specials

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bouzo.specials.backend.DealsRepo
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem

class DealsListViewModel(val dealListTitle: String, private val repo: DealsRepo) : ViewModel() {

    val canvasUnit: LiveData<CanvasInfo>
        get() = repo.canvasInfo()

    val dealsList: LiveData<List<DealItem>>
        get() = repo.dealsList()
}