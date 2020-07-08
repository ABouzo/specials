package com.bouzo.specials.backend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem
import com.bouzo.specials.restapi.DealsService
import kotlinx.coroutines.*

class DealsRepo(
    private val dealsService: DealsService,
    private val memoryCache:  MemoryCache<Pair<List<DealItem>, CanvasInfo>>
) {
    private val dealListData = MutableLiveData<List<DealItem>>()
    private val canvasInfoData = MutableLiveData<CanvasInfo>()

    fun dealsList(): LiveData<List<DealItem>> {
        GlobalScope.launch { fetchDeals() }
        return dealListData
    }

    fun canvasInfo(): LiveData<CanvasInfo> {
        GlobalScope.launch { fetchDeals() }
        return canvasInfoData
    }

    private suspend fun fetchDeals() = withContext(Dispatchers.IO) {
        synchronized(memoryCache) {
            when (memoryCache.isFresh()) {
                true -> fetchFromCache()
                false -> fetchFromApi()
            }
        }
    }

    private fun fetchFromCache() {
        dealListData.postValue(memoryCache.load()?.first)
        canvasInfoData.postValue(memoryCache.load()?.second)
    }

    private fun fetchFromApi() {

        val response = dealsService.getManagerSpecials().execute()

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
            dealListData.postValue(dealList)
            canvasInfoData.postValue(canvasInfo)
        }
    }

    private fun saveToMemory(dealList: List<DealItem>, canvasInfo: CanvasInfo) {
        memoryCache.save(Pair(dealList, canvasInfo))
    }
}