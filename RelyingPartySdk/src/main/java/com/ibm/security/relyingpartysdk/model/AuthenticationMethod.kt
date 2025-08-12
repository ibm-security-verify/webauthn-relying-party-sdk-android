/*
 * Copyright contributors to the IBM Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk.model

import kotlinx.serialization.Serializable
@Serializable
/**
 * An interface for generating an authentication method (see [Cookies] and [Token]).
 */
sealed interface AuthenticationMethod