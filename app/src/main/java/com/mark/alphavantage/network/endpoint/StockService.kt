package com.mark.alphavantage.network.endpoint

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {

    // https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=JPM&interval=1min&apikey=Z8EW6CI3PHR9SUTK
    companion object {
        const val STOCK_BASE = "query?function="
    }

    @GET(value = STOCK_BASE + "TIME_SERIES_INTRADAY")
    suspend fun getStockDetails(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String
    ): JsonObject

}