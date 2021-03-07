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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.shapes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier) {
    val viewModel: MainViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        counterDown(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            seconds = viewModel.clock
        )
        if (!viewModel.started) {
            timePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
            )
        }
        bottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )
    }
}

@Composable
fun counterDown(modifier: Modifier, seconds: Int) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val viewModel: MainViewModel = viewModel()
        val (progressBackground, progress, countdownTimer) = createRefs()

        CircularProgressIndicator(
            1f,
            modifier = Modifier
                .constrainAs(progressBackground) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .height(300.dp)
                .width(300.dp),
            strokeWidth = 32.dp, color = Color.LightGray
        )

        Progress(
            Modifier
                .constrainAs(progress) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .height(300.dp)
                .width(300.dp),
            countdownSeconds = viewModel.clock, totalSeconds = viewModel.totalClock
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(countdownTimer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = seconds.div(3600).toString(),
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ":",
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = seconds.rem(3600).div(60).toString(),
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ":",
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = seconds.rem(60).toString(),
                fontSize = 48.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Progress(modifier: Modifier = Modifier, countdownSeconds: Int, totalSeconds: Int) {
    val progress = if (totalSeconds == 0) 0f else countdownSeconds.toFloat() / totalSeconds
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = SpringSpec(Spring.DampingRatioNoBouncy, 5f, visibilityThreshold = 1 / 1000f)
    ).value

    CircularProgressIndicator(
        animatedProgress,
        modifier = modifier.fillMaxSize(),
        strokeWidth = 32.dp
    )
}

@Preview
@Composable
fun previewProgress() {
    Progress(countdownSeconds = 23, totalSeconds = 60)
}

@Composable
fun timePicker(modifier: Modifier) {
    val viewModel: MainViewModel = viewModel()
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { viewModel.incHour() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_inc),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = viewModel.hours.toString(),
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 8.dp)
            )
            IconButton(
                onClick = { viewModel.decHour() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dec),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { viewModel.incMinute() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_inc),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = viewModel.minutes.toString(),
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 8.dp)
            )
            IconButton(
                onClick = { viewModel.decMinute() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dec),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { viewModel.incSecond() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_inc),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
            Text(
                text = viewModel.seconds.toString(),
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(0.dp, 8.dp)
            )
            IconButton(
                onClick = { viewModel.decSecond() },
                Modifier
                    .background(
                        Color.LightGray,
                        shape = shapes.large
                    )
                    .size(32.dp, 32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dec),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun bottomMenu(modifier: Modifier) {
    val viewModel: MainViewModel = viewModel()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { viewModel.reset() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reset), contentDescription = null,
                modifier = Modifier
                    .size(32.dp),
                tint = Color.Black
            )
        }

        if (!viewModel.started) {
            IconButton(onClick = { viewModel.startCountdown() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_start), contentDescription = null,
                    modifier = Modifier
                        .size(32.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(modifier = Modifier.fillMaxSize())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(modifier = Modifier.fillMaxSize())
    }
}
