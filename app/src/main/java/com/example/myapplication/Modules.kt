package com.example.myapplication

import com.example.myapplication.backend.DealsRepo
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { DealsRepo() }
    viewModel { DealsListViewModel(get()) }
}