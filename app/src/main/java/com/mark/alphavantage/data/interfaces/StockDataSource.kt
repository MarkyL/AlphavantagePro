package com.mark.alphavantage.data.interfaces

import com.google.gson.JsonObject
import com.mark.alphavantage.network.model.responses.StockDetailsResponse


// Implement here all methods to be overridden & implemented by the repository
interface StockDataSource {
//    suspend fun temp(phoneNumber: String, uuid: String): LoginResponse

    suspend fun getStockDetails(symbol: String, interval: String) : JsonObject
}