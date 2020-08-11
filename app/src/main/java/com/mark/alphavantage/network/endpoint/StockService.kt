package com.mark.alphavantage.network.endpoint

interface StockService {

    companion object {
        const val PATIENT_BASE = "patient/"
    }

//    @POST(value = PATIENT_BASE + "login")
//    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}