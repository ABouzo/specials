package com.bouzo.specials.backend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem
import com.bouzo.specials.restapi.DealsService
import com.bouzo.specials.restapi.models.ManagerSpecial
import com.bouzo.specials.restapi.models.ManagerSpecialsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class DealsRepoTest {

    var dealsService: DealsService? = null
    var mockWebServer: MockWebServer? = null
    var memoryCache: SimpleMemoryCache<Pair<List<DealItem>, CanvasInfo>> = SimpleMemoryCache()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        memoryCache = SimpleMemoryCache()
        mockWebServer = MockWebServer()
        mockWebServer?.let {
            dealsService = Retrofit.Builder()
                .baseUrl(it.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DealsService::class.java) as DealsService
        }
    }

    @Test
    fun `data fetched from repo on stale cache`() = mockResponse { repo, server ->
        server.enqueue(MockResponse().setBody(MANAGER_SPECIAL_RESPONSE))
        val dealList = repo.dealsList()

        val expectedApiFetches = 1
        val lock = CountDownLatch(expectedApiFetches)

        dealList.observeForever {
            assertTrue(it.isNotEmpty())
            lock.countDown()
        }

        lock.await(500, TimeUnit.MILLISECONDS)
        assertEquals(expectedApiFetches, server.requestCount)
    }

    @Test
    fun `data fetched from memory on fresh cache`() = mockResponse { repo, server ->
        server.enqueue(MockResponse().setBody(MANAGER_SPECIAL_RESPONSE))
        server.enqueue(MockResponse().setBody("{}"))
        assertEquals(0, server.requestCount)

        val expectedFetches = 3
        val expectedApiFetches = 1

        val lock = CountDownLatch(expectedFetches)
        var count = 0

        repo.dealsList().observeForever {
            assertTrue(it.isNotEmpty())
            count++
            lock.countDown()
        }

        repo.dealsList()
        repo.dealsList()

        lock.await(500, TimeUnit.MILLISECONDS)
        assertEquals(expectedApiFetches, server.requestCount)
        assertEquals(expectedFetches, count)
    }

    @Test
    fun `data fetched and transformed correctly`() = mockResponse { repo, server ->
        server.enqueue(MockResponse().setBody(MANAGER_SPECIAL_RESPONSE))
        val gson = Gson()
        val specials: ManagerSpecialsResponse<ManagerSpecial> = gson.fromJson(
            MANAGER_SPECIAL_RESPONSE,
            object : TypeToken<ManagerSpecialsResponse<ManagerSpecial>>() {}.type
        )

        val expectedFetches = 1
        val lock = CountDownLatch(expectedFetches)

        repo.dealsList().observeForever {
            it.forEachIndexed { index, dealItem ->
                val special = specials.managerSpecials[index]
                dealItem.run {
                    assertEquals(special.displayName, dealName)
                    assertEquals(special.imageUrl, dealImageUrl)
                    assertEquals(special.price, price)
                    assertEquals(special.originalPrice, originalPrice)
                    assertEquals(special.width, width)
                    assertEquals(special.height, height)
                }
            }
            lock.countDown()
        }

        if (!lock.await(500, TimeUnit.MILLISECONDS)) fail()
        assertEquals(expectedFetches, server.requestCount)

    }

    @Test
    fun `data is updated if cache is stale after first call`() = mockResponse { repo, server ->
        server.enqueue(MockResponse().setBody(MANAGER_SPECIAL_RESPONSE))
        server.enqueue(MockResponse().setBody(MANAGER_SPECIAL_RESPONSE_2))

        val expectedFetches = 3
        val expectedApiFetches = 2
        var count = 0

        val lock = CountDownLatch(expectedFetches)

        var list: List<DealItem>? = null

        repo.dealsList().observeForever {
            count++
            assertTrue(it.isNotEmpty())

            if (count == 2) {
                //Second call should be from memory and not have changed
                assertEquals(list, it)
                //Now invalidate cache to make third request come from api
                memoryCache.cacheLifetime = 0L
            } else {
                assertNotEquals(list, it)
            }
            list = it
            lock.countDown()
        }

        repo.dealsList()
        repo.dealsList()

        if (!lock.await(500, TimeUnit.MILLISECONDS)) fail("Timeout")
        assertEquals(expectedApiFetches, server.requestCount)
    }

    private fun mockResponse(
        checks: (DealsRepo, MockWebServer) -> Unit
    ) {
        dealsService?.let { service ->
            mockWebServer?.let { server ->
                val repo = DealsRepo(service, memoryCache)
                checks(repo, server)

            } ?: fail("Failed at server mocking")
        } ?: fail("Failed at service init")
    }

    companion object {
        const val MANAGER_SPECIAL_RESPONSE: String = """
            {  
               "canvasUnit":16,
               "managerSpecials":[  
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/L.png",
                     "width":16,
                     "height":8,
                     "display_name":"Noodle Dish with Roasted Black Bean Sauce",
                     "original_price":"2.00",
                     "price":"1.00"
                  },
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/J.png",
                     "width":8,
                     "height":8,
                     "display_name":"Onion Flavored Rings",
                     "original_price":"2.00",
                     "price":"1.00"
                  },
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/K.png",
                     "width":8,
                     "height":8,
                     "display_name":"Kikkoman Less Sodium Soy Sauce",
                     "original_price":"2.00",
                     "price":"1.00"
                  }
               ]
            }
        """

        const val MANAGER_SPECIAL_RESPONSE_2: String = """
            {  
               "canvasUnit":12,
               "managerSpecials":[  
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/L.png",
                     "width":12,
                     "height":8,
                     "display_name":"Noodle Dish with Roasted Black Bean Sauce",
                     "original_price":"2.00",
                     "price":"1.00"
                  },
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/J.png",
                     "width":6,
                     "height":8,
                     "display_name":"Onion Flavored Rings",
                     "original_price":"2.00",
                     "price":"1.00"
                  },
                  {  
                     "imageUrl":"https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/images/K.png",
                     "width":6,
                     "height":8,
                     "display_name":"Kikkoman Less Sodium Soy Sauce",
                     "original_price":"2.00",
                     "price":"1.00"
                  }
               ]
            }
        """
    }
}