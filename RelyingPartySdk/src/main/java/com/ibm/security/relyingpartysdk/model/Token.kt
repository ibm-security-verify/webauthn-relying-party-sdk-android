/*
 * Copyright contributors to the IBM Security Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
/**
 * Represents an access token.
 */
data class Token(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiry: Int,
    @SerialName("id_token") val idToken: String? = null
) : AuthenticationMethod {
    val authorizationHeader: String
        get() = "$tokenType $accessToken"
}
