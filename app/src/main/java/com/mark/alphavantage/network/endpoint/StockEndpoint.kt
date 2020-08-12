package com.mark.alphavantage.network.endpoint

import com.google.gson.JsonObject
import com.mark.alphavantage.network.model.responses.StockDetailsResponse

class StockEndpoint constructor(private val stockService: StockService) {

    suspend fun getStockDetails(symbol: String): JsonObject {
        return stockService.getStockDetails(symbol)
    }
}