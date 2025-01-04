package com.andyc.core.data.auth

import kotlinx.serialization.Serializable

// We don't want to couple serialization library in the domain module, so we map the normal AuthInfo
// into the Serializable version in data module
@Serializable
data class SerializableAuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)