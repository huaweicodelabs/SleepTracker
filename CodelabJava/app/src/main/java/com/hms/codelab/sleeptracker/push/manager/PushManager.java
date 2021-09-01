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

package com.hms.codelab.sleeptracker.push.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.hms.codelab.sleeptracker.push.client.AccessTokenClient;
import com.hms.codelab.sleeptracker.push.client.NotificationClient;
import com.hms.codelab.sleeptracker.push.model.AccessToken;
import com.hms.codelab.sleeptracker.push.model.AccessTokenInterface;
import com.hms.codelab.sleeptracker.push.model.NotificationInterface;
import com.hms.codelab.sleeptracker.push.model.NotificationMessage;
import com.hms.codelab.sleeptracker.push.model.NotificationMessageBody;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;

import java.util.Collections;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushManager {
    private static PushManager instance;

    private PushManager() {

    }

    public static PushManager getInstance() {
        if (instance == null) {
            instance = new PushManager();
        }
        return instance;
    }

    private static String accessToken = null;
    private static String pushToken = null;

    public static void getDeviceIdToken(Context context) {
        (new Thread() {
            public void run() {
                try {
                    // TODO: Obtain the push token
                } catch (Exception e) {
                    Log.i(Constants.pushServiceTAG, "DeviceIdToken failed, " + e.getMessage());
                }
            }
        }).start();
    }

    public static void getAccessToken(String sleepType, Context context) {
        // TODO: Store the access token through SharedPreferences
    }

    private static NotificationMessageBody selectNotificationMessageBody(String sleepType, String pushToken) {
        // TODO: Show notifications on the notification bar by NotificationCompat according to the sleep scenario
    }

    private static void sendNotification(String pushToken, String sleepType) {
        // TODO: Send the notification
    }
}
