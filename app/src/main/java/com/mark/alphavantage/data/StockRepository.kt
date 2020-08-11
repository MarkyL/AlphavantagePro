package com.mark.alphavantage.data

import com.mark.alphavantage.data.interfaces.StockDataSource
import com.mark.alphavantage.data.mock.StockMockDataSource
import com.mark.alphavantage.data.remote.StockRemoteDataSource

class StockRepository constructor(remoteDataSource: StockRemoteDataSource) : StockDataSource {

    private val mockDataSource = StockMockDataSource()

    private var activeDataSource: StockDataSource = remoteDataSource
    //    private var activeDataSource: StockDataSource = mockDataSource

//    override suspend fun getStocks(): LoginResponse {
//        TODO("Not yet implemented")
//    }

}