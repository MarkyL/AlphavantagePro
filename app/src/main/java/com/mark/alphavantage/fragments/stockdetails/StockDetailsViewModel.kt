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
import com.mark.alphavantage.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class StockDetailsViewModel constructor(application: Application, private val stockRepository: StockRepository)
    : BaseViewModel<Event<StockDetailsDataState>, StockDetailsDataEvent>(application) {

    override fun handleScreenEvents(event: StockDetailsDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event) {
            is GetStockDetails -> getStockDetails(event.symbol)
        }
    }

    private fun getStockDetails(symbol: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("getStockDetails - runCatching")
                publish(state = State.LOADING)
                stockRepository.getStockDetails(symbol)
            }.onSuccess {
                Timber.i("getStockDetails - success")
                publish(
                    state = State.NEXT,
                    items = Event(GetStockDetailsSuccess(StockDetailsResponse.convertJsonToStockDetailsResponse(it))))
            }.onFailure {
                Timber.i("getStockDetails - failure ($it)")
                publish(state = State.ERROR, throwable = it)
            }
        }
    }

    private fun convertJsonToStockDetailsResponse(jsonObject: JsonObject) {
        val stockDetailsResponse =
            StockDetailsResponse.convertJsonToStockDetailsResponse(jsonObject)
    }

}

// Events = actions coming from UI
sealed class StockDetailsDataEvent
data class GetStockDetails(val symbol: String): StockDetailsDataEvent()

// States = returned back to UI
sealed class StockDetailsDataState
data class GetStockDetailsSuccess(val stockDetailsResponse: StockDetailsResponse): StockDetailsDataState()