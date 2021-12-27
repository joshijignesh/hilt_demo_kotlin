package com.kisancalculator.data.repositories

import com.kisancalculator.data.network.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class UsersRepository @Inject constructor(private val mApiService: ApiServices) {

    suspend fun fetchUsers(page: Int): Response<String> {
        return withContext(Dispatchers.IO) {
            return@withContext mApiService.fetchUsers(page)
        }
    }

}