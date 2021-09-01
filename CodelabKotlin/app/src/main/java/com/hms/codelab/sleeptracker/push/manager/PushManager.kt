
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
package com.hms.codelab.sleeptracker.push.manager

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.hms.codelab.sleeptracker.push.client.AccessTokenClient
import com.hms.codelab.sleeptracker.push.client.NotificationClient
import com.hms.codelab.sleeptracker.push.model.*
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.hms.codelab.sleeptracker.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PushManager {
    companion object {
        private var accessToken: String? = null
        private var pushToken: String? = null

        fun getDeviceIdToken(context: Context) {
            object : Thread() {
                override fun run() {
                    try {
                        val appId: String =
                            AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                        pushToken = HmsInstanceId.getInstance(context).getToken(appId, "HCM")

                        if (!TextUtils.isEmpty(pushToken)) {
                            val sharedPreferences = context.getSharedPreferences(
                                Constants.packageName,
                                Context.MODE_PRIVATE
                            )
                            sharedPreferences.edit().putString(Constants.pushTokenStr, pushToken).apply()
                            Log.i(Constants.pushServiceTAG, "get token: $pushToken")
                        }
                    } catch (e: Exception) {
                        Log.i(Constants.pushServiceTAG, "DeviceIdToken failed, $e")
                    }
                }
            }.start()
        }

        fun getAccessToken(sleepType: String, context: Context) {
            AccessTokenClient.getClient().create(AccessTokenInterface::class.java)
                .createAccessToken(
                    Constants.grantType,
                    Constants.appSecret,
                    Constants.appId
                ).enqueue(object : Callback<AccessToken> {
                    override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                        Log.e(Constants.pushServiceTAG, "Error: ${t.message}")
                    }

                    override fun onResponse(
                        call: Call<AccessToken>,
                        response: Response<AccessToken>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(
                                Constants.pushServiceTAG,
                                "Access Token: ${response.body()?.accessToken}"
                            )
                            val sharedPreferences = context.getSharedPreferences(
                                Constants.packageName,
                                Context.MODE_PRIVATE
                            )
                            val sharedPushToken = sharedPreferences.getString(Constants.pushTokenStr, pushToken)
                            accessToken = response.body()?.accessToken
                            sharedPushToken?.let { sendNotification(it, sleepType) }
                        }
                    }
                })
        }

        private fun selectNotificationMessageBody(
            sleepType: String,
            pushToken: String
        ): NotificationMessageBody {
            when (sleepType) {
                Constants.sleepStr -> {
                    return NotificationMessageBody.Builder(
                        Constants.goodNightTitle, Constants.goodNightBody[((0..4).random())],
                        arrayOf(pushToken)
                    ).build()
                }
                Constants.wakeStr -> {
                    return NotificationMessageBody.Builder(
                        Constants.goodMorningTitle,
                        Constants.goodMorningBody[(0..5).random()],
                        arrayOf(pushToken)
                    ).build()
                }
                Constants.sleepReportStr -> {
                    return NotificationMessageBody.Builder(
                        Constants.sleepReportTitle, Constants.sleepReportBody,
                        arrayOf(pushToken)
                    ).build()
                }
                else -> {
                    return NotificationMessageBody.Builder(
                        Constants.notificationErrorTitle, Constants.notificationErrorBody,
                        arrayOf(pushToken)
                    ).build()
                }
            }
        }

        private fun sendNotification(pushToken: String, sleepType: String) {
            NotificationClient.getClient().create(NotificationInterface::class.java)
                .createNotification(
                    "Bearer $accessToken",
                    selectNotificationMessageBody(sleepType, pushToken)
                ).enqueue(object : Callback<NotificationMessage> {
                    override fun onResponse(
                        call: Call<NotificationMessage>,
                        response: Response<NotificationMessage>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(Constants.pushServiceTAG, "Response ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<NotificationMessage>, t: Throwable) {
                        Log.d(Constants.pushServiceTAG, "Error: ${t.message}")
                    }
                })
        }
    }
}