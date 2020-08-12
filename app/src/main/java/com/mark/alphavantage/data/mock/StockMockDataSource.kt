package com.mark.alphavantage.data.mock

import com.google.gson.JsonObject
import com.mark.alphavantage.data.interfaces.StockDataSource
import com.mark.alphavantage.network.model.responses.StockData
import com.mark.alphavantage.network.model.responses.StockDetailsResponse
import com.mark.alphavantage.network.model.responses.StockMetaData
import com.mark.alphavantage.network.model.responses.StockTimeSeries

class StockMockDataSource : StockDataSource {
    override suspend fun getStockDetails(symbol: String): JsonObject {
        return JsonObject() //StockDetailsResponse(StockMetaData(), "")//∂StockTimeSeries(listOf()))
    }
}
