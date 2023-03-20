/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
data class FIDO2Verification(
    val clientDataJson: String,
    val authenticatorData: String,
    val credentialId: String,
    val signature: String,
    val userHandle: String
)
