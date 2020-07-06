package com.example.myapplication.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem
import com.example.myapplication.datamodels.ShelfList
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


    private val dealListData = MutableLiveData<ShelfList>()
    private val canvasInfo = MutableLiveData<CanvasInfo>()

    init {
        dealDao.loadAll().observeForever {
            it?.let {
                val list = it.map { dealEntity ->
                    DealItem(
                        dealImageUrl = dealEntity.dealUrl,
                        dealName = dealEntity.dealName,
                        width = dealEntity.dealWidth,
                        height = dealEntity.dealHeight,
                        originalPrice = dealEntity.originalPrice,
                        price = dealEntity.price
                    )
                }
                dealListData.postValue(ShelfList(canvasInfo.value?.canvasUnit ?: 1, list))
            }
        }


        canvasDao.getCanvas().observeForever { canvasEntity ->
            canvasEntity?.let {
                canvasInfo.postValue(
                    CanvasInfo(
                        canvasUnit = canvasEntity.canvasUnit
                    )
                )
            }
        }
    }

    fun dealsList(): LiveData<ShelfList> {
        GlobalScope.launch { fetchDeals() }
        return dealListData
    }

    fun canvasInfo(): LiveData<CanvasInfo> {
        GlobalScope.launch { fetchDeals() }
        return canvasInfo
    }

    private suspend fun fetchDeals() = withContext(Dispatchers.IO) {
        val response = dealsService.getManagerSpecials().execute()

        response.body()?.run {

            val dealList = managerSpecials.map { managerSpecial ->
                DealEntity(
                    dealName = managerSpecial.displayName,
                    dealUrl = managerSpecial.imageUrl,
                    dealHeight = managerSpecial.height,
                    dealWidth = managerSpecial.width,
                    originalPrice = managerSpecial.originalPrice,
                    price = managerSpecial.price
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