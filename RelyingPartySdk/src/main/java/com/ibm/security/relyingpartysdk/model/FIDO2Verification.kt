/*
 * Copyright contributors to the IBM Security Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FIDO2Verification(
    val clientDataJSON: String,
    val authenticatorData: String,
    val credentialId: String,
    val signature: String,
    val userHandle: String
)
