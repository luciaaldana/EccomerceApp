package com.luciaaldana.eccomerceapp.data.auth.network

import com.luciaaldana.eccomerceapp.data.auth.dto.UserRegistrationDto
import com.luciaaldana.eccomerceapp.data.auth.dto.LoginDto
import com.luciaaldana.eccomerceapp.data.auth.dto.LoginResponse
import com.luciaaldana.eccomerceapp.data.auth.dto.UpdateUserDto
import com.luciaaldana.eccomerceapp.data.auth.dto.UpdateUserResponse
import com.luciaaldana.eccomerceapp.core.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @POST("/users/register")
    suspend fun registerUser(@Body userRegistration: UserRegistrationDto): Response<User>
    
    @POST("/users/login")
    suspend fun loginUser(@Body loginData: LoginDto): Response<LoginResponse>
    
    @PUT("/users/update/{email}")
    suspend fun updateUser(
        @Path("email") email: String,
        @Body userData: UpdateUserDto
    ): Response<User>
}