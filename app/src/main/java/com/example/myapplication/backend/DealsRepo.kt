package com.example.myapplication.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.datamodels.CanvasInfo
import com.example.myapplication.datamodels.DealItem
import com.example.myapplication.datamodels.ShelfList
import com.example.myapplication.restapi.DealsService
import com.example.myapplication.room.CanvasDao
import com.example.myapplication.room.CanvasEntity
import com.example.myapplication.room.DealDao
import com.example.myapplication.room.DealEntity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class DealsRepo(
    private val dealsService: DealsService,
    private val dealDao: DealDao,
    private val canvasDao: CanvasDao
) {
    private val dealListData = MutableLiveData<ShelfList>()
    private val canvasInfoData = MutableLiveData<CanvasInfo>()
    private var simpleMemCache: ShelfList? = null
    private var canvasInfoCache: CanvasInfo? = null
    private var lastRefresh: Date = Date(0)

    fun dealsList(): LiveData<ShelfList> {
        GlobalScope.launch { fetchDeals() }
        return dealListData
    }

    fun canvasInfo(): LiveData<CanvasInfo> {
        GlobalScope.launch { fetchDeals() }
        return canvasInfoData
    }

    init {
        canvasInfoData.observeForever {
            simpleMemCache?.let { cached ->
                simpleMemCache = ShelfList(it.canvasUnit, cached.allDealItem)
                dealListData.postValue(simpleMemCache)
            }
        }
    }

    private suspend fun fetchDeals() = withContext(Dispatchers.IO) {
        val response = dealsService.getManagerSpecials().execute()

        val now = Date().time
        if (now - lastRefresh.time < TimeUnit.HOURS.toMillis(1)
            && simpleMemCache != null
            && canvasInfoCache != null
        ) {
            dealListData.postValue(simpleMemCache)
            canvasInfoData.postValue(canvasInfoCache)
            return@withContext
        }

        response.body()?.run {
            val canvasInfo = CanvasInfo(canvasUnit.toInt())
            val dealList = managerSpecials.map { managerSpecial ->
                DealItem(
                    dealName = managerSpecial.displayName,
                    dealImageUrl = managerSpecial.imageUrl,
                    height = managerSpecial.height,
                    width = managerSpecial.width,
                    originalPrice = managerSpecial.originalPrice,
                    price = managerSpecial.price
                )
            }
            saveToMemory(dealList, canvasInfo)
            dealListData.postValue(ShelfList(canvasInfo.canvasUnit, dealList))
            canvasInfoData.postValue(canvasInfo)
        }
    }

    private fun saveToMemory(dealList: List<DealItem>, canvasInfo: CanvasInfo) {
        lastRefresh = Date()
        simpleMemCache = ShelfList(canvasInfo.canvasUnit, dealList)
        canvasInfoCache = canvasInfo
    }

    private fun saveToDao(dealList: List<DealItem>, canvasInfo: CanvasInfo) {
        val canvasUnit = canvasInfo.canvasUnit

        val dealList = dealList.map { managerSpecial ->
            DealEntity(
                dealName = managerSpecial.dealName,
                dealUrl = managerSpecial.dealImageUrl,
                dealHeight = managerSpecial.height,
                dealWidth = managerSpecial.width,
                originalPrice = managerSpecial.originalPrice,
                price = managerSpecial.price
            )
        }

        dealDao.clearDb()
        dealDao.save(*dealList.toTypedArray())

        canvasDao.getCanvas().value?.let {
            if (it.canvasUnit != canvasUnit) {
                canvasDao.save(CanvasEntity(CanvasDao.DEFAULT_ID, canvasUnit))
            }
        } ?: canvasDao.save(CanvasEntity(CanvasDao.DEFAULT_ID, canvasUnit))
    }

}