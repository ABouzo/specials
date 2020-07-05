package com.example.myapplication

import androidx.room.Room
import com.example.myapplication.backend.DealsRepo
import com.example.myapplication.restapi.DealsService
import com.example.myapplication.room.AppDB
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
        DealsRepo(
            get(),
            get<AppDB>().dealDao(),
            get<AppDB>().canvasDao()
        )
    }

    factory { GsonConverterFactory.create() as Converter.Factory }

    viewModel { DealsListViewModel(get()) }
}