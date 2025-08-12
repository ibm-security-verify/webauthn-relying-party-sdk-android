/*
 * Copyright contributors to the IBM Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
data class OTPChallenge(val transactionId: String, val correlation: String, val expiry: Instant)