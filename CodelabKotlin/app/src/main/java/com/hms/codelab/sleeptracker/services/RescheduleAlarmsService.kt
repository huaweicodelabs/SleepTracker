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

import android.content.Intent

import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.hms.codelab.sleeptracker.alarm.service.AlarmService
import com.hms.codelab.sleeptracker.utils.Constants

class RescheduleAlarmsService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle(Constants.rescheduleTitle)
            .setContentText(Constants.rescheduleBody)
            .build()
        startForeground(1002, notification)
        val sharedPreferences = this.getSharedPreferences(
            Constants.packageName,
            android.content.Context.MODE_PRIVATE
        )
        val wakeUpTime =
            sharedPreferences.getString(Constants.wakeUpTimeStr, Constants.defaultWakeUpTime)
        var hour = wakeUpTime!!.take(2)
        var minute = wakeUpTime.takeLast(2)
        val alarmService = AlarmService(this)
        val switchState = sharedPreferences.getBoolean(Constants.alarmSwitchState, false)
        if (minute.toInt() >= 30) {
            val newHour = hour.toInt() + 1
            val newMin = minute.toInt() - 30
            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.wakeStr, 1)
            alarmService.setRepetitiveAlarm(
                newHour + 2,
                newMin,
                Constants.sleepReportStr,
                2
            )
            if (switchState) {
                alarmService.setRepetitiveAlarm(
                    hour.toInt(),
                    minute.toInt(),
                    Constants.alarmClockStr,
                    3
                )
            }

        } else {
            alarmService.setRepetitiveAlarm(hour.toInt(), minute.toInt() + 30, Constants.wakeStr, 1)
            alarmService.setRepetitiveAlarm(
                hour.toInt() + 2,
                minute.toInt(),
                Constants.sleepReportStr,
                2
            )
            if (switchState) {
                alarmService.setRepetitiveAlarm(
                    hour.toInt(),
                    minute.toInt(),
                    Constants.alarmClockStr,
                    3
                )
            }
        }
        val sleepTime =
            sharedPreferences.getString(Constants.sleepTimeStr, Constants.defaultSleepTime)
        hour = sleepTime!!.take(2)
        minute = sleepTime.takeLast(2)
        if (minute.toInt() >= 30) {
            val newHour = hour.toInt()
            val newMin = minute.toInt() - 30
            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.sleepStr, 0)
        } else {
            alarmService.setRepetitiveAlarm(
                hour.toInt(),
                minute.toInt() - 30,
                Constants.sleepStr,
                0
            )
        }
        stopForeground(true)
        return super.onStartCommand(intent, flags, startId)
    }

}