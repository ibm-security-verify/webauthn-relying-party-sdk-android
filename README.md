# IBM Security Verify Relying Party SDK for Android

## Overview

The IBM Security Verify Relying Party SDK for Android is the client-side companion to [IBM Security Verify Relying Party Server](https://github.com/ibm-security-verify/webauthn-relying-party-server-swift) which exposes REST API's hosted in a Docker image.

RelyingPartySDK is a lightweight framework that provides the ability for existing users to register their device with Passkey and subsequently sign-in without a password and for new users to sign-up and validate their account.

Go to the [Android Developer Site](https://passkeys.dev/docs/reference/android/) if you like to learn more about developing Passkey-enabled apps.

### Getting started
The RelyingPartySDK is available as an APK file [here](https://github.com/ibm-security-verify/webauthn-relying-party-sdk-android/releases).

## Contents
### Allowing a user to sign-up
This scenario is where the user doesn't exist in your identity system and requires ownership of an email address for validation.

```java
    // The baseURL is the host of the relying party server.
    val client = RelyingPartyClient(URL("https://www.example.com"))
    
    // The result is an OTPChallenge to correlate the email sent to the email address.
    val result = client.signup("scott", "scott@mailinator.com")
    
    // Use the result.transactionId and the OTP value generated in the email to validate. If successful, the returned Token can be used to register for Passkey.
    result.onSuccess { 
        val token = client.validate(it.transactionId, "123456")
    }
```

### Authenticate an existing user
This scenario authenticates an existing user with a username and password.

```java
    // The baseURL is the host of the relying party server.
    val client = RelyingPartyClient(URL("https://www.example.com"))

    val token = client.authenticate("scott@mailinator.com", "passw0rd")

    token.onSuccess {
        // The result is a Token which can be used to register for Passkey.
    }
```

### Registering for Passkey
This scenario requires the user to be authenticated with a valid `Token`. Registering for Passkey uses ASAuthorizationControllerDelegate to handle the result of the platform key registration request.