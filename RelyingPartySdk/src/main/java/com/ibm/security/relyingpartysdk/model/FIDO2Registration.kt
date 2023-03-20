/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable

@Serializable
data class FIDO2Registration(
    val nickName: String,
    val clientDataJson: String,
    val attestationObject: String,
    val credentialId: String
)
