/*
 * Copyright contributors to the IBM Security Verify Relying Party SDK for Android project
 */

package com.ibm.security.relyingpartysdk

import com.ibm.security.relyingpartysdk.model.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                val result = httpClient.post<Token> {
                    url("$baseURL".plus("/v1/authenticate"))
                    body = UserAuthentication(username, password)
                }
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
                val result = httpClient.post<OTPChallenge> {
                    url("$baseURL".plus("/v1/signup"))
                    body = UserSignUp(name, email)
                }
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
                val result = httpClient.post<Token> {
                    url("$baseURL".plus("/v1/validate"))
                    body = OTPVerification(transactionId, otp)
                }
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()
    }

    /**
     * A request to generate a WebAuthn challenge.
     *
     * @param type  The type of FIDO2 challenge.
     * @param displayName   The display name used by the authenticator for UI representation.
     * @param token Represents an access token.
     *
     * @return  The [FIDO2Challenge] for registration.
     */
    suspend fun challenge(
        type: ChallengeType,
        displayName: String? = null,
        token: Token? = null
    ): Result<FIDO2Challenge> {

        val result = coroutineScope.async {
            try {
                val result = httpClient.post<FIDO2Challenge> {
                    header("Authentication", token?.authorizationHeader())
                    url("$baseURL".plus("/v1/challenge"))
                    body = ChallengeRequest(displayName, type)
                }
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
        clientDataJson: Json,
        attestationObject: Json,
        credentialId: String,
        token: Token
    ): Result<Unit> {

        val registration = FIDO2Registration(
            nickname,
            Base64.getUrlEncoder()
                .encodeToString(Json.encodeToString(clientDataJson).toByteArray()),
            Base64.getUrlEncoder()
                .encodeToString(Json.encodeToString(attestationObject).toByteArray()),
            Base64.getUrlEncoder().encodeToString(Json.encodeToString(credentialId).toByteArray())
        )

        val result = coroutineScope.async {
            try {
                val result = httpClient.post<Unit> {
                    header("Authentication", token.authorizationHeader())
                    url("$baseURL".plus("/v1/register"))
                    body = registration
                }
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
        signature: Json,
        clientDataJson: Json,
        authenticatorData: Json,
        credentialId: String,
        userId: String
    ): Result<Token> {

        val verification = FIDO2Verification(
            Base64.getUrlEncoder()
                .encodeToString(Json.encodeToString(clientDataJson).toByteArray()),
            Base64.getUrlEncoder()
                .encodeToString(Json.encodeToString(authenticatorData).toByteArray()),
            Base64.getUrlEncoder().encodeToString(Json.encodeToString(credentialId).toByteArray()),
            Base64.getUrlEncoder().encodeToString(Json.encodeToString(signature).toByteArray()),
            Base64.getUrlEncoder().encodeToString(Json.encodeToString(userId).toByteArray())
        )

        val result = coroutineScope.async {
            try {
                val result = httpClient.post<Token> {
                    url("$baseURL".plus("/v1/signin"))
                    body = verification
                }
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        return result.await()

    }

}