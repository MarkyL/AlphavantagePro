package com.mark.alphavantage.di

import com.mark.alphavantage.fragments.main.MainViewModel
import com.mark.alphavantage.fragments.stockdetails.StockDetailsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(androidApplication()) }
    viewModel { StockDetailsViewModel(androidApplication(), get()) }
}

// Gather all app modules
val stockApp = listOf(
    viewModelsModule,
    retrofitModule)
