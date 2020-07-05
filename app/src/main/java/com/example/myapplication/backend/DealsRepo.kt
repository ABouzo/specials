package com.example.myapplication.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem
import com.example.myapplication.restapi.DealsService
import com.example.myapplication.room.CanvasDao
import com.example.myapplication.room.CanvasEntity
import com.example.myapplication.room.DealEntity
import com.example.myapplication.room.DealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DealsRepo(
    private val dealsService: DealsService,
    private val dealDao: DealDao,
    private val canvasDao: CanvasDao
) {

    fun dealsList(): LiveData<List<DealItem>> {
        val liveData = MutableLiveData<List<DealItem>>()

        //GlobalScope.launch { fetchDeals() }

        dealDao.loadAll().observeForever {
            it?.let {
                liveData.postValue(it.map { dealEntity ->
                    DealItem(
                        dealImageUrl = dealEntity.dealUrl,
                        dealName = dealEntity.dealName
                    )
                })
            }
        }

        return liveData
    }

    fun canvasInfo(): LiveData<CanvasInfo> {
        val liveData = MutableLiveData<CanvasInfo>()

        GlobalScope.launch { fetchDeals() }

        canvasDao.getCanvas().observeForever { canvasEntity ->
            canvasEntity?.let {
                liveData.postValue(
                    CanvasInfo(
                        canvasUnit = canvasEntity.canvasUnit
                    )
                )
            }
        }

        return liveData
    }

    private suspend fun fetchDeals() = withContext(Dispatchers.IO) {
        val response = dealsService.getManagerSpecials().execute()

        response.body()?.run {

            val dealList = managerSpecials.map { managerSpecial ->
                DealEntity(
                    dealName = managerSpecial.displayName,
                    dealUrl = managerSpecial.imageUrl
                )
            }

            dealDao.clearDb()
            dealDao.save(*dealList.toTypedArray())

            canvasDao.getCanvas().value?.let {
                if (it.canvasUnit != canvasUnit.toInt()) {
                    canvasDao.save(CanvasEntity(CanvasDao.DEFAULT_ID, canvasUnit.toInt()))
                }
            } ?: canvasDao.save(CanvasEntity(CanvasDao.DEFAULT_ID, canvasUnit.toInt()))
        }
    }

}