package com.mark.alphavantage.data

import com.google.gson.JsonObject
import com.mark.alphavantage.data.interfaces.StockDataSource
import com.mark.alphavantage.data.mock.StockMockDataSource
import com.mark.alphavantage.data.remote.StockRemoteDataSource
import com.mark.alphavantage.network.model.responses.StockDetailsResponse

class StockRepository constructor(remoteDataSource: StockRemoteDataSource) : StockDataSource {

    private val mockDataSource = StockMockDataSource()

    private var activeDataSource: StockDataSource = remoteDataSource
//    private var activeDataSource: StockDataSource = mockDataSource

    override suspend fun getStockDetails(symbol: String, interval: String): JsonObject {
        return activeDataSource.getStockDetails(symbol, interval)
    }
}