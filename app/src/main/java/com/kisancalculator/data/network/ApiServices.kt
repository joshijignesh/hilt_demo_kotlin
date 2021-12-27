package com.kisancalculator.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET(EndPoints.FETCH_USERS)
    suspend fun fetchUsers(@Query("page")page:Int): Response<String>
}