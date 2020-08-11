package com.mark.alphavantage.di

import com.mark.alphavantage.fragments.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(androidApplication()) }
}




// Gather all app modules
val stockApp = listOf(
    viewModelsModule
)
