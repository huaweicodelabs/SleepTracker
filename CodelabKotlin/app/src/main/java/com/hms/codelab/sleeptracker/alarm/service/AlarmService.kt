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
package com.hms.codelab.sleeptracker.alarm.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hms.codelab.sleeptracker.alarm.receiver.AlarmReceiver
import com.hms.codelab.sleeptracker.utils.Constants
import java.util.*

class AlarmService(context: Context) {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val intent = Intent(context, AlarmReceiver::class.java)
    private val localContext = context

    fun setRepetitiveAlarm(selectedHour: Int, selectedMin: Int, timeType: String, reqCode: Int) {
        Log.i(Constants.pushServiceTAG, "${Constants.alarmServiceStr}: Hour: $selectedHour, Min: $selectedMin")
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMin)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)


        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }

        when (timeType) {
            Constants.sleepStr -> {
                intent.putExtra(Constants.sleepTypeStr, timeType)
                val pendingIntentSleep: PendingIntent =
                    PendingIntent.getBroadcast(
                        localContext,
                        reqCode,
                        intent,
                        0
                    )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntentSleep
                )
            }
            Constants.wakeStr -> {
                intent.putExtra(Constants.sleepTypeStr, timeType)
                val pendingIntentWake: PendingIntent =
                    PendingIntent.getBroadcast(
                        localContext,
                        reqCode,
                        intent,
                        0
                    )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntentWake
                )
            }
            Constants.sleepReportStr -> {
                intent.putExtra(Constants.sleepTypeStr, timeType)
                val pendingIntentReport: PendingIntent =
                    PendingIntent.getBroadcast(
                        localContext,
                        reqCode,
                        intent,
                        0
                    )
                alarmManager.setRepeating(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntentReport
                )
            }
            Constants.alarmClockStr -> {
                intent.putExtra(Constants.sleepTypeStr, timeType)
                val pendingIntentAlarmClock: PendingIntent =
                    PendingIntent.getBroadcast(
                        localContext,
                        reqCode,
                        intent,
                        0
                    )
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntentAlarmClock
                )
            }
        }
    }

    fun cancelAlarm(reqCode: Int) {
        val pendingIntent = PendingIntent.getBroadcast(localContext, reqCode, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}
