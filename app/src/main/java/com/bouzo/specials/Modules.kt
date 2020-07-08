package com.bouzo.specials

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bouzo.specials.backend.DealsRepo
import com.bouzo.specials.backend.MemoryCache
import com.bouzo.specials.backend.SimpleMemoryCache
import com.bouzo.specials.datamodels.CanvasInfo
import com.bouzo.specials.datamodels.DealItem
import com.bouzo.specials.flexbox.FlexboxRecyclerAdapter
import com.bouzo.specials.restapi.DealsService
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.squareup.picasso.Picasso
import okhttp3.HttpUrl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(get<HttpUrl>())
            .addConverterFactory(get())
            .build()
            .create(DealsService::class.java) as DealsService
    }

    single {
        Picasso.Builder(get())
            .build()
    }

    single {
        DealsRepo(
            get(),
            get()
        )
    }

    factory { GsonConverterFactory.create() as Converter.Factory }

    factory { FlexboxRecyclerAdapter(get(), get()) as DealsRecyclerAdapter }

    factory {
        FlexboxLayoutManager(get()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
        } as RecyclerView.LayoutManager
    }

    factory {
        SimpleMemoryCache<Pair<List<DealItem>, CanvasInfo>>() as MemoryCache<Pair<List<DealItem>, CanvasInfo>>
    }

    factory {
        HttpUrl.Builder()
            .scheme("https")
            .host("raw.githubusercontent.com")
            .build() as HttpUrl
    }

    viewModel {
        DealsListViewModel(
            get<Context>().resources.getString(R.string.deal_list_page_title),
            get()
        ) as DealsListViewModel
    }
}