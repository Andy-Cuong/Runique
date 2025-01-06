package com.andyc.auth.data

import com.andyc.auth.domain.AuthRepository
import com.andyc.core.data.networking.post
import com.andyc.core.domain.AuthInfo
import com.andyc.core.domain.SessionStorage
import com.andyc.core.domain.util.DataError
import com.andyc.core.domain.util.EmptyResult
import com.andyc.core.domain.util.Result
import com.andyc.core.domain.util.asEmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )

        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }

        return result.asEmptyResult()
    }

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