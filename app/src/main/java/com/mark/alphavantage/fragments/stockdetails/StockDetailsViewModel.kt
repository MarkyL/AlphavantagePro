package com.mark.alphavantage.fragments.stockdetails

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mark.alphavantage.data.StockRepository
import com.mark.alphavantage.mvvm.BaseViewModel
import com.mark.alphavantage.mvvm.State
import com.mark.alphavantage.network.model.responses.StockData
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.network.model.responses.StockMetaData
import com.mark.alphavantage.network.model.responses.StockTimeSeries
import com.mark.alphavantage.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.RuntimeException

class StockDetailsViewModel constructor(application: Application, private val stockRepository: StockRepository)
    : BaseViewModel<Event<StockDetailsDataState>, StockDetailsDataEvent>(application) {

    override fun handleScreenEvents(event: StockDetailsDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event) {
            is GetStockDetails -> getStockDetails(event.symbol, event.timeInterval)
        }
    }

    private fun getStockDetails(symbol: String, timeInterval: StockDetailsResponse.TimeInterval) {
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("getStockDetails - runCatching with symbol = $symbol, interval = ${timeInterval.typeName}")
                publish(state = State.LOADING)
                stockRepository.getStockDetails(symbol, timeInterval.typeName)
            }.onSuccess {
                Timber.i("getStockDetails - success")
                try {
                    publish(
                        state = State.NEXT,
                        items = Event(GetStockDetailsSuccess(StockDetailsResponse.convertJsonToStockDetailsResponse(it, timeInterval))))
                }  catch (exception: RuntimeException) {
                    // Happens due to API allowing up to 5 requests per minute.
                    Timber.e("Failed convertJsonToStockDetailsResponse - $exception")
                    publish(state = State.ERROR, throwable = Throwable(exception))
                }
            }.onFailure {
                Timber.i("getStockDetails - failure ($it)")
                publish(state = State.ERROR, throwable = it)
            }
        }
    }

}

// Events = actions coming from UI
sealed class StockDetailsDataEvent
data class GetStockDetails(val symbol: String, val timeInterval: StockDetailsResponse.TimeInterval): StockDetailsDataEvent()

// States = returned back to UI
sealed class StockDetailsDataState
data class GetStockDetailsSuccess(val stockDetailsResponse: StockDetailsResponse): StockDetailsDataState()