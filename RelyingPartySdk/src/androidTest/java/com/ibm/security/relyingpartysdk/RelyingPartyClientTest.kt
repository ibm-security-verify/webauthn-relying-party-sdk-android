package com.ibm.security.relyingpartysdk

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ibm.security.relyingpartysdk.model.ChallengeType
import com.ibm.security.relyingpartysdk.model.UserSignUp
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RelyingPartyClientTest {
    @Test
    fun test_useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ibm.security.relyingpartysdk", appContext.packageName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_authorize() = runTest {
        val result =
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud")).authenticate(
                "scott",
                "!Passw0rd"
            )

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_signUp() = runTest {
        val result =
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud")).signup(
                "scott",
                "scott@mailinator.com"
            )

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_validate() = runTest {
        val result =
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud"))
                .validate("<UUID>", "123456")

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_challengeAssertion() = runTest {
        val result =
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud"))
                .challenge(ChallengeType.ASSERTION)

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_challengeAttestation() = runTest {

        RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud")).authenticate(
            "scott",
            "!Passw0rd"
        ).onSuccess { token ->
            Log.d("Success", "Result: $token")
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud"))
                .challenge(ChallengeType.ATTESTATION, token = token)
                .onSuccess { fido2challenge ->
                    Log.d("Success", "Result: $fido2challenge")
                }
                .onFailure {
                    Log.d("Failure", "Result: ${it.message}")
                    fail()
                }
        }.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_register() = runTest {

        RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud")).authenticate(
            "scott",
            "!Passw0rd"
        ).onSuccess { token ->
            Log.d("Success", "Result: $token")
            RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud"))
                .register(
                    "John's Android Phone",
                    clientDataJson = Json,
                    attestationObject = Json,
                    credentialId = "credentialId",
                    token = token
                )
                .onSuccess {
                    Log.d("Success", "")
                }
                .onFailure {
                    Log.d("Failure", "Result: ${it.message}")
                    fail()
                }
        }.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_signin() = runTest {
        RelyingPartyClient(URL("https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud"))
            .signing(
                signature = Json, clientDataJson = Json, authenticatorData = Json,
                credentialId = "", userId = "userId"
            )
            .onSuccess {
                Log.d("Success", "Result: $it")
            }
            .onFailure {
                Log.d("Failure", "Result: ${it.message}")
                fail()
            }
    }
}