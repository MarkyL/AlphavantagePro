package com.mark.alphavantage.data.remote

import com.google.gson.JsonObject
import com.mark.alphavantage.data.interfaces.StockDataSource
import com.mark.alphavantage.network.endpoint.StockEndpoint
import com.mark.alphavantage.network.model.responses.StockDetailsResponse

class StockRemoteDataSource constructor(private val endpoint: StockEndpoint) : StockDataSource {

    override suspend fun getStockDetails(symbol: String): JsonObject {
        return endpoint.getStockDetails(symbol)
    }

}
