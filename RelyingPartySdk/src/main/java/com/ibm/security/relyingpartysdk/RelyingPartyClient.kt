/*
 * Copyright contributors to the IBM Security Verify WebAuthn Relying Party SDK for Android
 */

@file:Suppress("unused")

package com.ibm.security.relyingpartysdk

import com.ibm.security.relyingpartysdk.model.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.net.URL
import java.util.*

@SuppressWarnings("Unused")
class RelyingPartyClient(private val baseURL: URL) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val httpClient by lazy { NetworkHelper.getInstance }

    /**
     * The user authentication request.
     *
     * @param username  The user's username.
     * @param password  The users' password.
     *
     * @return A [Token] representing an authenticated user.
     */
    suspend fun authenticate(username: String, password: String): Result<Token> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    url("$baseURL".plus("/v1/authenticate"))
                    contentType(ContentType.Application.Json)
                    setBody(UserAuthentication(username, password))
                }.body<Token>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * Allows the user to sign up for an account.
     *
     * @param name  The formatted name of the user.
     * @param email The email address of the user.
     *
     * @return A [OTPChallenge] structure that describes a one-time password challenge.
     */
    suspend fun signup(name: String, email: String): Result<OTPChallenge> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    url("$baseURL".plus("/v1/signup"))
                    contentType(ContentType.Application.Json)
                    setBody(UserSignUp(name, email))
                }.body<OTPChallenge>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * @param transactionId The unique identifier of the verification.
     * @param otp  The one-time password value
     *
     * @return A [Token] representing an authenticated user.
     */
    suspend fun validate(transactionId: String, otp: String): Result<Token> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    url("$baseURL".plus("/v1/validate"))
                    contentType(ContentType.Application.Json)
                    setBody(OTPVerification(transactionId, otp))
                }.body<Token>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * A request to generate a WebAuthn challenge for attestation.
     *
     * @param displayName   The display name used by the authenticator for UI representation.
     * @param token Represents an access token.
     *
     * @return  The [PublicKeyCredentialOption] as JSON formatted string.
     */
    suspend fun challengeAttestation(
        displayName: String? = null,
        token: Token? = null
    ): Result<String> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    header("Authorization", token?.authorizationHeader())
                    url("$baseURL".plus("/v1/challenge"))
                    contentType(ContentType.Application.Json)
                    setBody(ChallengeRequest(displayName, "attestation"))
                }.body<String>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * A request to generate a WebAuthn challenge for assertion.
     *
     * @param displayName   The display name used by the authenticator for UI representation.
     * @param token Represents an access token.
     *
     * @return  The [PublicKeyCredential](https://developer.android.com/reference/androidx/credentials/PublicKeyCredential#PublicKeyCredential(kotlin.String))
     *          as JSON formatted string.
     */
    suspend fun challengeAssertion(
        displayName: String? = null,
        token: Token? = null
    ): Result<String> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    header("Authorization", token?.authorizationHeader())
                    url("$baseURL".plus("/v1/challenge"))
                    contentType(ContentType.Application.Json)
                    setBody(ChallengeRequest(displayName, "assertion"))
                }.body<String>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * A request to present an attestation object containing a public key to the server for
     * attestation verification and storage.
     *
     * @param nickname The friendly name for the registration.
     * @param clientDataJson Raw data that contains a JSON-compatible encoding of the client data.
     * @param attestationObject A data object that contains the returned attestation.
     * @param credentialId  An identifier that the authenticator generates during registration to uniquely identify a specific credential.
     * @param token Represents an access token.
     *
     * @return The [Result] of the network request
     */
    suspend fun register(
        nickname: String,
        clientDataJson: String,
        attestationObject: String,
        credentialId: String,
        token: Token
    ): Result<Unit> {

        val registration = FIDO2Registration(
            nickname,
            clientDataJson,
            attestationObject,
            credentialId
        )

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    header("Authorization", token.authorizationHeader())
                    url("$baseURL".plus("/v1/register"))
                    contentType(ContentType.Application.Json)
                    setBody(registration)
                }.body<Unit>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * A request to present the signed challenge to the server for verification.
     *
     * @param signature The signature for the assertion.
     * @param clientDataJson Raw data that contains a JSON-compatible encoding of the client data.
     * @param authenticatorData A byte sequence that contains additional information about the credential.
     * @param credentialId An identifier that the authenticator generates during registration to uniquely identify a specific credential.
     * @param userId The userId provided when creating this credential.
     *
     * @return A [Token] representing an authenticated user.
     */
    suspend fun signing(
        signature: String,
        clientDataJson: String,
        authenticatorData: String,
        credentialId: String,
        userId: String
    ): Result<Token> {

        val verification = FIDO2Verification(
            clientDataJson,
            authenticatorData,
            credentialId,
            signature,
            userId
        )

        val result = coroutineScope.async {
            try {
                val result = httpClient.post {
                    url("$baseURL".plus("/v1/signin"))
                    contentType(ContentType.Application.Json)
                    setBody(verification)
                }.body<Token>()
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()

    }
}