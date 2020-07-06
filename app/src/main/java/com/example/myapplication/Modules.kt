package com.example.myapplication

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.backend.DealsRepo
import com.example.myapplication.flexbox.FlexboxRecyclerAdapter
import com.example.myapplication.restapi.DealsService
import com.example.myapplication.room.AppDB
import com.example.myapplication.shelfs.ShelfRecyclerAdapter
import com.google.android.flexbox.*
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(DealsService.baseUrl)
            .addConverterFactory(get())
            .build()
            .create(DealsService::class.java) as DealsService
    }

    single {
        Room.databaseBuilder(
            get(),
            AppDB::class.java,
            "deals-database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Picasso.Builder(get())
            .build()
    }

    single {
        DealsRepo(
            get(),
            get<AppDB>().dealDao(),
            get<AppDB>().canvasDao()
        )
    }

    factory { GsonConverterFactory.create() as Converter.Factory }

    //factory { ShelfRecyclerAdapter(get(), get()) as DealsRecyclerAdapter }
    factory { FlexboxRecyclerAdapter(get(), get()) as DealsRecyclerAdapter }

//    factory { LinearLayoutManager(get()) as RecyclerView.LayoutManager }
    factory {
        FlexboxLayoutManager(get()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
        } as RecyclerView.LayoutManager
    }
    viewModel { DealsListViewModel(get()) }
}