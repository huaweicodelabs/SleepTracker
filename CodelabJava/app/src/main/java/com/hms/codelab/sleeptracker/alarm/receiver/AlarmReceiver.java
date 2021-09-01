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

package com.hms.codelab.sleeptracker.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hms.codelab.sleeptracker.services.AlarmClock;
import com.hms.codelab.sleeptracker.services.PushNotificationService;
import com.hms.codelab.sleeptracker.services.RescheduleAlarmsService;
import com.hms.codelab.sleeptracker.utils.Constants;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String sleepType = intent.getStringExtra(Constants.sleepTypeStr);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleAlarmsService(context);
        } else {
            if(sleepType.equals(Constants.alarmClockStr)) {
                startAlarmService(context);
            } else {
                startPushNotificationService(context, intent);
            }
        }
    }

    private void startRescheduleAlarmsService(Context context){
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        context.startForegroundService(intentService);
    }

    private void startAlarmService(Context context){
        Intent intentService = new Intent(context, AlarmClock.class);
        context.startForegroundService(intentService);
    }

    private void startPushNotificationService(Context context, Intent intent){
        Intent intentService = new Intent(context, PushNotificationService.class);
        intentService.putExtra(Constants.sleepTypeStr, intent.getStringExtra(Constants.sleepTypeStr));
        context.startForegroundService(intentService);
    }

}
