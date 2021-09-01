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

package com.hms.codelab.sleeptracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.hms.codelab.sleeptracker.utils.Constants;
import com.hms.codelab.sleeptracker.utils.KoinJavaUtils;

public class SleepTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KoinJavaUtils.startKoin(this);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                Constants.CHANNEL_ID,
                "Alarm Clock Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }
}
