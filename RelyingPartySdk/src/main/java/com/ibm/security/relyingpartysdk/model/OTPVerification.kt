/*
 * Copyright contributors to the IBM Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class OTPVerification(val transactionId: String, val otp: String)
