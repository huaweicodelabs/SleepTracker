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
                    String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
                    pushToken = HmsInstanceId.getInstance(context).getToken(appId, "HCM");

                    if (!TextUtils.isEmpty(pushToken)) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(
                                Constants.packageName,
                                Context.MODE_PRIVATE
                        );
                        sharedPreferences.edit().putString(Constants.pushTokenStr, pushToken).apply();
                        Log.i(Constants.pushServiceTAG, "Get Token: " + pushToken);
                    }
                } catch (Exception e) {
                    Log.i(Constants.pushServiceTAG, "DeviceIdToken failed, " + e.getMessage());
                }
            }
        }).start();
    }

    public static void getAccessToken(String sleepType, Context context) {
        AccessTokenClient.getClient().create(AccessTokenInterface.class)
                .createAccessToken(Constants.grantType, Constants.appSecret, Constants.appId).enqueue(
                new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                SharedPreferences sharedPreferences = context.getSharedPreferences(
                                        Constants.packageName,
                                        Context.MODE_PRIVATE
                                );
                                String sharedPushToken = sharedPreferences.getString(Constants.pushTokenStr, pushToken);
                                accessToken = response.body().getAccessToken();
                                if (sharedPushToken != null) {
                                    sendNotification(sharedPushToken, sleepType);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.e(Constants.pushServiceTAG, "Error: " + t.getMessage());
                    }
                }
        );
    }

    private static NotificationMessageBody selectNotificationMessageBody(String sleepType, String pushToken) {
        Random rand = new Random();
        switch (sleepType) {
            case Constants.sleepStr:
                return new NotificationMessageBody.Builder(
                        Constants.goodNightTitle,
                        Constants.goodNightBody.get(rand.nextInt(Constants.goodNightBody.size())),
                        Collections.singletonList(pushToken)
                ).build();
            case Constants.wakeStr:
                return new NotificationMessageBody.Builder(
                        Constants.goodMorningTitle,
                        Constants.goodMorningBody.get(rand.nextInt(Constants.goodNightBody.size())),
                        Collections.singletonList(pushToken)
                ).build();
            case Constants.sleepReportStr:
                return new NotificationMessageBody.Builder(
                        Constants.sleepReportTitle,
                        Constants.sleepReportBody,
                        Collections.singletonList(pushToken)
                ).build();
            default:
                return new NotificationMessageBody.Builder(
                        Constants.notificationErrorTitle,
                        Constants.notificationErrorBody,
                        Collections.singletonList(pushToken)
                ).build();
        }
    }

    private static void sendNotification(String pushToken, String sleepType) {
        NotificationClient.getClient().create(NotificationInterface.class)
                .createNotification("Bearer " + accessToken,
                        selectNotificationMessageBody(sleepType, pushToken)).enqueue(
                new Callback<NotificationMessage>() {
                    @Override
                    public void onResponse(Call<NotificationMessage> call, Response<NotificationMessage> response) {
                        if (response.isSuccessful()) {
                            Log.d(Constants.pushServiceTAG, "Response " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationMessage> call, Throwable t) {
                        Log.e(Constants.pushServiceTAG, "Error: " + t.getMessage());
                    }
                }
        );
    }
}
