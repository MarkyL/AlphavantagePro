package com.mark.alphavantage.network.model.responses

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.mark.alphavantage.utils.DateTimeHelper
import timber.log.Timber
import java.lang.RuntimeException
import java.text.DecimalFormat
import kotlin.collections.ArrayList


class StockDetailsResponse(
    @SerializedName(META_DATA) val metaData: StockMetaData,
    @SerializedName(TIME_SERIES_ONE) val stockTimeSeries: StockTimeSeries
) {

    companion object {
        private const val META_DATA = "Meta Data"
        private const val TIME_SERIES_ONE = "Time Series (1min)"
        private const val TIME_SERIES_FIVE = "Time Series (5min)"
        private const val TIME_SERIES_FIFTEEN = "Time Series (15min)"
        private const val TIME_SERIES_THIRTY = "Time Series (30min)"
        private const val TIME_SERIES_SIXTY = "Time Series (60min)"

        fun convertJsonToStockDetailsResponse(json: JsonObject, timeInterval: TimeInterval): StockDetailsResponse {
            try {
                val metaData = json.getAsJsonObject(META_DATA)
                val stockMetaData = StockMetaData.convertJsonToStockMetaData(metaData)

                val stockTimeSeriesJson = json.getAsJsonObject(timeInterval.type)
                val stockTimeSeries =
                    StockTimeSeries.convertJsonToStockTimeSeries(stockTimeSeriesJson)

                return StockDetailsResponse(
                    metaData = stockMetaData,
                    stockTimeSeries = stockTimeSeries
                )
            } catch (exception: RuntimeException) {
                // Happens due to API allowing up to 5 requests per minute.
                Timber.e("Failed convertJsonToStockDetailsResponse - $exception")
                return StockDetailsResponse(StockMetaData(), StockTimeSeries())
            }
        }
    }

    // 1min, 5min, 15min, 30min, 60min
    enum class TimeInterval(val type: String, val typeName: String) {
        ONE(TIME_SERIES_ONE, "1min"),
        FIVE(TIME_SERIES_FIVE, "5min"),
        FIFTEEN(TIME_SERIES_FIFTEEN, "15min"),
        THIRTY(TIME_SERIES_THIRTY, "30min"),
        SIXTY(TIME_SERIES_SIXTY, "60min")
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
                information = if (json.has(INFORMATION)) json.get(INFORMATION).asString else "",
                symbol = if (json.has(SYMBOL)) json.get(SYMBOL).asString else "",
                lastRefreshed = if (json.has(LAST_REFRESHED)) json.get(LAST_REFRESHED).asString else "",
                interval = if (json.has(INTERVAL)) json.get(INTERVAL).asString else "",
                outputSize = if (json.has(OUTPUT_SIZE)) json.get(OUTPUT_SIZE).asString else "",
                timeZone = if (json.has(TIME_ZONE)) json.get(TIME_ZONE).asString else ""
            )
        }
    }
}

class StockTimeSeries(val stockList: ArrayList<StockData> = arrayListOf()) {

    companion object {
        fun convertJsonToStockTimeSeries(json: JsonObject): StockTimeSeries {
            val keys = json.keySet()
            val stockList: ArrayList<StockData> = arrayListOf()

            keys.forEach { key ->
                val stockJson = json.getAsJsonObject(key)

                DateTimeHelper.getDateFormat(key)?.let { timeStamp ->
                    val dateTokens = key.split(" ")
                    val stockData = StockData.convertJsonToStockData(
                        stockJson,
                        timeStamp = timeStamp, date = dateTokens[0], time = dateTokens[1])
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
                time = formatTimeWithSeconds(time),
                open = if (json.has(OPEN)) formatPrice(json.get(OPEN).asString) else "",
                high = if (json.has(HIGH)) formatPrice(json.get(HIGH).asString) else "",
                low = if (json.has(LOW)) formatPrice(json.get(LOW).asString) else "",
                close = if (json.has(CLOSE)) formatPrice(json.get(CLOSE).asString) else "",
                volume = if (json.has(VOLUME)) formatPrice(json.get(VOLUME).asString) else "")
        }

        /*
        A function to format a time from "HH:mm:ss" format to "HH:mm" if seconds are meaningless.
         */
        private fun formatTimeWithSeconds(time: String): String {
            val tokens = time.split(":")
            if (tokens.size == 3 && tokens[2].isNotEmpty() && tokens[2] == "00") {
                return tokens[0] + ":" + tokens[1]
            }
            return time
        }

        /*
        A function to format a price string like "103.9300" to "103.93"
        --> Deleting trailing zeroes.
         */
        private fun formatPrice(priceString: String): String {
            val priceDouble = priceString.toDouble()
            val decimalFormat = DecimalFormat("0.####")
            return decimalFormat.format(priceDouble)
        }
    }
}

