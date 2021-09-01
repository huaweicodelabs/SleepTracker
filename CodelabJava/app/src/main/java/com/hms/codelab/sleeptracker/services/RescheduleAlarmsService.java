/*
 Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License

 */

package com.hms.codelab.sleeptracker.services;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.hms.codelab.sleeptracker.alarm.service.AlarmService;
import com.hms.codelab.sleeptracker.utils.Constants;

public class RescheduleAlarmsService extends LifecycleService {
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setContentTitle(Constants.rescheduleTitle)
                .setContentText(Constants.rescheduleBody)
                .build();
        startForeground(1002, notification);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                Constants.packageName,
                Context.MODE_PRIVATE
        );
        String wakeUpTime = sharedPreferences.getString(Constants.wakeUpTimeStr, Constants.defaultWakeUpTime);
        String hour = wakeUpTime.substring(0, 2);
        String minute = wakeUpTime.substring(wakeUpTime.length() - 2);
        AlarmService alarmService = new AlarmService(this);
        boolean switchState = sharedPreferences.getBoolean(Constants.alarmSwitchState, false);
        if (Integer.parseInt(minute) >= 30) {
            int newHour = Integer.parseInt(hour) + 1;
            int newMin = Integer.parseInt(minute) - 30;
            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.wakeStr, 1);
            alarmService.setRepetitiveAlarm(newHour + 2, newMin, Constants.sleepReportStr, 2);
        } else {
            alarmService.setRepetitiveAlarm(Integer.parseInt(hour), Integer.parseInt(minute) + 30, Constants.wakeStr, 1);
            alarmService.setRepetitiveAlarm(Integer.parseInt(hour) + 2, Integer.parseInt(minute), Constants.sleepReportStr, 2);
        }
        if (switchState) {
            alarmService.setRepetitiveAlarm(
                    Integer.parseInt(hour), Integer.parseInt(minute), Constants.alarmClockStr, 3);
        }
        String sleepTime = sharedPreferences.getString(Constants.sleepTimeStr, Constants.defaultSleepTime);
        hour = sleepTime.substring(0, 2);
        minute = sleepTime.substring(sleepTime.length() - 2);
        if (Integer.parseInt(minute) >= 30) {
            int newHour = Integer.parseInt(hour);
            int newMin = Integer.parseInt(minute) - 30;
            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.sleepStr, 0);
        } else {
            alarmService.setRepetitiveAlarm(
                    Integer.parseInt(hour),
                    Integer.parseInt(minute) - 30,
                    Constants.sleepStr,
                    0
            );
        }
        stopForeground(true);
        return super.onStartCommand(intent, flags, startId);
    }
}
