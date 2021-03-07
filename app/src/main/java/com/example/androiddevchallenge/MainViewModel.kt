/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var started by mutableStateOf(false)
    var totalClock by mutableStateOf(0)
    var clock by mutableStateOf(0)
    var hours by mutableStateOf(0)
    var minutes by mutableStateOf(0)
    var seconds by mutableStateOf(0)

    var job: Job? = null

    fun reset() {
        if (started && job != null) {
            job?.cancel()
            started = false
            totalClock = 0
            clock = 0
        }
        hours = 0
        minutes = 0
        seconds = 0
    }

    fun startCountdown() {
        started = true
        totalClock = hours * 3600 + minutes * 60 + seconds
        clock = totalClock
        job = viewModelScope.launch(Dispatchers.IO) {
            while (clock > 0) {
                delay(1000)
                clock = clock.dec()
            }
            reset()
        }
    }

    fun incHour() {
        hours = hours.inc()
    }

    fun decHour() {
        hours = if (hours > 0) hours.dec() else 0
    }

    fun incMinute() {
        minutes = minutes.inc()
    }

    fun decMinute() {
        minutes = if (minutes > 0) minutes.dec() else 0
    }

    fun incSecond() {
        seconds = seconds.inc()
    }

    fun decSecond() {
        seconds = if (seconds > 0) seconds.dec() else 0
    }
}
