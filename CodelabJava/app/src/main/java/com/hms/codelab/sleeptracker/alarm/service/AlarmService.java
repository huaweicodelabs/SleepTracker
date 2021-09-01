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

package com.hms.codelab.sleeptracker.alarm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hms.codelab.sleeptracker.alarm.receiver.AlarmReceiver;
import com.hms.codelab.sleeptracker.utils.Constants;

import java.util.Calendar;

public class AlarmService {

    private final AlarmManager alarmManager;
    private final Intent intent;
    private final Context localContext;

    public void setRepetitiveAlarm(int selectedHour, int selectedMin, String timeType, int reqCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMin);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (timeType.equals(Constants.sleepStr)) {
            intent.putExtra(Constants.sleepTypeStr, timeType);
            PendingIntent pendingIntentSleep = PendingIntent.getBroadcast(localContext, reqCode, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentSleep);
        }
        if (timeType.equals(Constants.wakeStr)) {
            intent.putExtra(Constants.sleepTypeStr, timeType);
            PendingIntent pendingIntentWake = PendingIntent.getBroadcast(localContext, reqCode, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentWake);
        }
        if (timeType.equals(Constants.sleepReportStr)) {
            intent.putExtra(Constants.sleepTypeStr, timeType);
            PendingIntent pendingIntentReport = PendingIntent.getBroadcast(localContext, reqCode, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentReport);
        }
        if (timeType.equals(Constants.alarmClockStr)) {
            intent.putExtra(Constants.sleepTypeStr, timeType);
            PendingIntent pendingIntentAlarmClock = PendingIntent.getBroadcast(localContext, reqCode, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentAlarmClock);
        }
    }

    public void cancelAlarm(int reqCode) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.localContext, reqCode, this.intent, 0);
        this.alarmManager.cancel(pendingIntent);
    }

    public AlarmService(Context context) {
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.intent = new Intent(context, AlarmReceiver.class);
        this.localContext = context;
    }
}
