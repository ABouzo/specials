package com.example.myapplication

import com.example.myapplication.backend.DealsRepo
import com.example.myapplication.restapi.DealsService
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {
    single { DealsRepo(get()) }

    single {
        Retrofit.Builder()
            .baseUrl(DealsService.baseUrl)
            .addConverterFactory(get())
            .build()
            .create(DealsService::class.java) as DealsService
    }

    factory { GsonConverterFactory.create() as Converter.Factory }

    viewModel { DealsListViewModel(get()) }
}