package com.andyc.core.data.auth

import com.andyc.core.domain.AuthInfo

fun AuthInfo.toSerializable(): SerializableAuthInfo {
    return SerializableAuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}

fun SerializableAuthInfo.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}