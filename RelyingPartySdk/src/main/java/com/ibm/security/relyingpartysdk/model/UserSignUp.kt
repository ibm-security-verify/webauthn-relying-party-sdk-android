/*
 * Copyright contributors to the IBM Security Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class UserSignUp(val name: String, val email: String)
