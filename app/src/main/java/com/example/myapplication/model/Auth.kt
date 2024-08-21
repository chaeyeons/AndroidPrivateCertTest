package com.example.myapplication.model
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val refresh: String,
    val access: String
)

interface AuthService {
    @POST("api/v1/auth/token/")
    suspend fun getToken(@Body authRequest: AuthRequest): Response<AuthResponse>
}
