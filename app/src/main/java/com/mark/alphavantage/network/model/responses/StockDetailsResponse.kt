package com.mark.alphavantage.network.model.responses

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.mark.alphavantage.utils.DateTimeHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StockDetailsResponse(
    @SerializedName(META_DATA) val metaData: StockMetaData,
    @SerializedName(TIME_SERIES_ONE) val stockTimeSeries: StockTimeSeries
) {

    companion object {
        private const val META_DATA = "Meta Data"
        private const val TIME_SERIES_ONE = "Time Series (1min)"

        fun convertJsonToStockDetailsResponse(json: JsonObject): StockDetailsResponse {
            val metaData = json.getAsJsonObject(META_DATA)
            val stockMetaData = StockMetaData.convertJsonToStockMetaData(metaData)

            val stockTimeSeriesJson = json.getAsJsonObject(TIME_SERIES_ONE)
            val stockTimeSeries = StockTimeSeries.convertJsonToStockTimeSeries(stockTimeSeriesJson)

            return StockDetailsResponse(metaData = stockMetaData, stockTimeSeries = stockTimeSeries)
        }
    }
}

class StockMetaData(
    @SerializedName(INFORMATION) val information: String = "",
    @SerializedName(SYMBOL) val symbol: String = "",
    @SerializedName(LAST_REFRESHED) val lastRefreshed: String = "",
    @SerializedName(INTERVAL) val interval: String = "",
    @SerializedName(OUTPUT_SIZE) val outputSize: String = "",
    @SerializedName(TIME_ZONE) val timeZone: String = ""
) {

    companion object {
        private const val INFORMATION = "1. Information"
        private const val SYMBOL = "2. Symbol"
        private const val LAST_REFRESHED = "3. Last Refreshed"
        private const val INTERVAL = "4. Interval"
        private const val OUTPUT_SIZE = "5. Output Size"
        private const val TIME_ZONE = "6. Time Zone"

        fun convertJsonToStockMetaData(json: JsonObject): StockMetaData {
            return StockMetaData(
                information = json.get(INFORMATION).asString,
                symbol = json.get(SYMBOL).asString,
                lastRefreshed = json.get(LAST_REFRESHED).asString,
                interval = json.get(INTERVAL).asString,
                outputSize = json.get(OUTPUT_SIZE).asString,
                timeZone = json.get(TIME_ZONE).asString
            )
        }
    }
}

class StockTimeSeries(val stockMap: ArrayList<StockData>) {

    companion object {
        fun convertJsonToStockTimeSeries(json: JsonObject): StockTimeSeries {
            val keys = json.keySet()
            val stockList: ArrayList<StockData> = arrayListOf()

            val cal = Calendar.getInstance()
            keys.forEach { key ->
                val stockJson = json.getAsJsonObject(key)

                DateTimeHelper.getDateFormat(key)?.let {
                    val split = key.split(" ")
                    val stockData = StockData.convertJsonToStockData(stockJson, it, split[0], split[1])
                    stockList.add(stockData)
                }
            }

            return StockTimeSeries(stockList)
        }
    }
}

class StockData(
    val timeStamp: Long,
    val time: String = "",
    val date: String = "",
    @SerializedName(OPEN) val open: String = "",
    @SerializedName(HIGH) val high: String = "",
    @SerializedName(LOW) val low: String = "",
    @SerializedName(CLOSE) val close: String = "",
    @SerializedName(VOLUME) val volume: String = ""
) {

    companion object {
        private const val OPEN = "1. open"
        private const val HIGH = "2. high"
        private const val LOW = "3. low"
        private const val CLOSE = "4. close"
        private const val VOLUME = "5. volume"

        fun convertJsonToStockData(json: JsonObject, timeStamp: Long, date: String, time: String): StockData {
            return StockData(
                timeStamp = timeStamp,
                date = date,
                time = time,
                open = json.get(OPEN).asString,
                high = json.get(HIGH).asString,
                low = json.get(LOW).asString,
                close = json.get(CLOSE).asString,
                volume = json.get(VOLUME).asString
            )
        }
    }
}

