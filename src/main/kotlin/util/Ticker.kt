package com.aznos.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

/**
 * Schedules [body] every [period]. The first call happens immediately
 */
inline fun CoroutineScope.schedule(period: Duration, crossinline body: suspend() -> Unit) = launch {
    while(isActive) {
        body()
        delay(period)
    }
}