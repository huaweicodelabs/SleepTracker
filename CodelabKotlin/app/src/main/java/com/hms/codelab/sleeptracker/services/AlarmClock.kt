
/*
 * *
 *  *Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *
 *
 *
 */
package com.hms.codelab.sleeptracker.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.ui.alarm.AlarmScanActivity
import com.hms.codelab.sleeptracker.utils.Constants

class AlarmClock : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer.isLooping = true

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, AlarmScanActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 3, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle(Constants.alarmTitle)
            .setContentText(Constants.alarmBody)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentIntent(pendingIntent)
            .build()

        mediaPlayer.start()

        val pattern = 100L
        vibrator.vibrate(VibrationEffect.createOneShot(pattern, VibrationEffect.DEFAULT_AMPLITUDE))
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.stop()
        vibrator.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}