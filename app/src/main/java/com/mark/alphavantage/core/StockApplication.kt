package com.mark.alphavantage.core

import androidx.multidex.MultiDexApplication
import com.mark.alphavantage.di.stockApp
import com.mark.alphavantage.navigation.Arguments
import com.mark.alphavantage.navigation.arguments.TransferInfo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class StockApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StockApplication)
            androidLogger()
            modules(stockApp)
        }

        registerFragmentArguments()

        Timber.plant(Timber.DebugTree())
    }

    private fun registerFragmentArguments() {
        Arguments.registerSubclass(TransferInfo::class.java)
    }

}