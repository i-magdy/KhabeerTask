package com.devwarex.kabeertask.api

import com.devwarex.kabeertask.models.EmployeeModel
import com.devwarex.kabeertask.models.LogInModel
import com.devwarex.kabeertask.models.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {


    @Headers(
        "Accept: application/json;charset=UTF-8"
    )
    @POST("Salamtak/LogIn")
    suspend fun logIn(
        @Body logIn: LogInModel = LogInModel()
    ): Response<UserModel>

    @GET("Salamtak/GetPayroll")
    suspend fun getEmployee(
        @Header("Authorization") token: String
    ): Response<EmployeeModel>
}