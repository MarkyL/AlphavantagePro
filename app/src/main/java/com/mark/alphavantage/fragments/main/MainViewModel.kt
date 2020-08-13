package com.mark.alphavantage.fragments.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.mark.alphavantage.data.StockRepository
import com.mark.alphavantage.model.StockModel
import com.mark.alphavantage.mvvm.BaseViewModel
import com.mark.alphavantage.mvvm.State
import com.mark.alphavantage.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.io.InputStream

class MainViewModel constructor(application: Application)//, private val stockRepository: StockRepository)
    : BaseViewModel<Event<MainDataState>, MainDataEvent>(application) {

    override fun handleScreenEvents(event: MainDataEvent) {
        Timber.i("dispatchScreenEvent: ${event.javaClass.simpleName}")
        when(event) {
            is GetStocks -> getStocks()
        }
    }

    private fun getStocks() {
        viewModelScope.launch {
            kotlin.runCatching {
                Timber.i("getStocks - run")
                val stocksJson = loadJSONFromAsset(getApplication())
                if (stocksJson.isNullOrEmpty()) {
                    Timber.i("getStocks - error loading stocks json.")
                    publish(state = State.ERROR, items = Event(GetStocksFailureState))
                } else {
                    val stocksList =
                        parseStocksJson(stocksJson).sortedByDescending { it.priority.toInt() }
                    Timber.i("getStocks - success loading stocks json.")
                    publish(state = State.NEXT, items = Event(GetStocksSuccess(stocksList)))
                }
            }
        }
    }

    private fun parseStocksJson(stocksJson: String?): List<StockModel> {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        return gson.fromJson(stocksJson, Array<StockModel>::class.java).toList()
    }

    private fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        json = try {
            val inputStream: InputStream = context.assets.open(STOCKS_JSON)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    companion object {
        private const val STOCKS_JSON = "stocks.json"
    }
}

// Events = actions coming from UI
sealed class MainDataEvent
object GetStocks: MainDataEvent()
data class GetStocksSuccess(val stocksList: List<StockModel>) : MainDataState()

// States = returned back to UI
sealed class MainDataState
object GetStocksFailureState: MainDataState()