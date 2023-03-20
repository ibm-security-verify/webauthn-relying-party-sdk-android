/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class OTPChallenge(val transactionId: String, val correlation: String, val expiry: Instant)