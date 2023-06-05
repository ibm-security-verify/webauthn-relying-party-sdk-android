/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FIDO2Registration(
    val nickname: String,
    val clientDataJSON: String,
    val attestationObject: String,
    val credentialId: String
)
