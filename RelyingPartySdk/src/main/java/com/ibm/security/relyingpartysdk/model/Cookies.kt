/*
 * Copyright contributors to the IBM Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
/**
 * Represent cookie-based session headers.
 *
 * @param items   A map of HTTP cookie headers that define an authenticated session
 */
data class Cookies(val items: Map<String, String>) : AuthenticationMethod {
    operator fun get(key: String): String? = items[key]
}