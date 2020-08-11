package com.mark.alphavantage.data.remote

import com.mark.alphavantage.data.interfaces.StockDataSource
import com.mark.alphavantage.network.endpoint.StockEndpoint

class StockRemoteDataSource constructor(private val endpoint: StockEndpoint) : StockDataSource {

//    override suspend fun temp(phoneNumber: String, uuid: String): LoginResponse {
//        TODO("Not yet implemented")
//    }


}
