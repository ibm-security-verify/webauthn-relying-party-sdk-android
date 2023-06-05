package com.ibm.security.relyingpartysdk

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
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

    private val serverUrl = "" // e.g. https://rps-app.vb76iz9iykg.au-syd.codeengine.appdomain.cloud
    private val username = "my username"
    private val password = "my password"

    @Test
    fun test_useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ibm.security.relyingpartysdk", appContext.packageName)
    }

    @Test
    fun test_authorize() = runTest {
        val result =
            RelyingPartyClient(URL(serverUrl)).authenticate(username, password)

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @Test
    fun test_signUp() = runTest {
        val result =
            RelyingPartyClient(URL(serverUrl)).signup(username, password)

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @Test
    fun test_validate() = runTest {
        val result =
            RelyingPartyClient(URL(serverUrl))
                .validate("<UUID>", "123456")

        result.onSuccess {
            Log.d("Success", "Result: $it")
        }
        result.onFailure {
            Log.d("Failure", "Result: ${it.message}")
            fail()
        }
    }

    @Test
    fun test_challengeAssertion() = runTest {

        RelyingPartyClient(URL(serverUrl)).authenticate(username, password)
            .onSuccess { token ->
                RelyingPartyClient(URL(serverUrl))
                    .challengeAssertion("my name", token = token)
                    .onSuccess {
                        Log.d("Success", "Result: $it")
                    }
                    .onFailure {
                        Log.d("Failure", "Result: ${it.message}")
                        fail()
                    }
            }
    }

    @Test
    fun test_challengeAttestation() = runTest {

        RelyingPartyClient(URL(serverUrl)).authenticate(username, password)
            .onSuccess { token ->
                Log.d("Success", "Result: $token")
                RelyingPartyClient(URL(serverUrl))
                    .challengeAttestation("my name", token = token)
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

    @Test
    fun test_register() = runTest {

        RelyingPartyClient(URL(serverUrl)).authenticate(username, password)
            .onSuccess { token ->
                Log.d("Success", "Result: $token")
                RelyingPartyClient(URL(serverUrl))
                    .register(
                        "John's Android Phone",
                        clientDataJson = "",
                        attestationObject = "",
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

    @Test
    fun test_signin() = runTest {
        RelyingPartyClient(URL(serverUrl))
            .signing(
                signature = "", clientDataJson = "", authenticatorData = "",
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