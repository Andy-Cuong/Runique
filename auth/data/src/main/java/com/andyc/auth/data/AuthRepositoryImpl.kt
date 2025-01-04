package com.andyc.auth.data

import com.andyc.auth.domain.AuthRepository
import com.andyc.core.data.networking.post
import com.andyc.core.domain.util.DataError
import com.andyc.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}