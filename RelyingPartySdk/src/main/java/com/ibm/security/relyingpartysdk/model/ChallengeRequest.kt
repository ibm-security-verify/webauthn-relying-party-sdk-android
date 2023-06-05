/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeRequest(val displayName: String?, val type: String)
