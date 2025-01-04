package com.andyc.auth.domain

import com.andyc.core.domain.util.DataError
import com.andyc.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}