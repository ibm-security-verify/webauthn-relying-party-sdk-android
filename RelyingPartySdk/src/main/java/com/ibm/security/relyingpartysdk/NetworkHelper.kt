/*
 * Copyright contributors to the IBM Security Verify WebAuthn Relying Party SDK for Android
 */

package com.ibm.security.relyingpartysdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Suppress("unused")
object NetworkHelper {

    private val client = HttpClient(engineFactory = CIO) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 15000L
            requestTimeoutMillis = 15000L
        }
    }

    val getInstance = client

}