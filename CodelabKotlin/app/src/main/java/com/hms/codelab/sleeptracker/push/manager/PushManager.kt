
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
                        TODO("Obtain the push token")
                    } catch (e: Exception) {
                        Log.i(Constants.pushServiceTAG, "DeviceIdToken failed, $e")
                    }
                }
            }.start()
        }

        fun getAccessToken(sleepType: String, context: Context) {
            TODO("Store the access token through SharedPreferences")
        }

        private fun selectNotificationMessageBody(
            sleepType: String,
            pushToken: String
        ): NotificationMessageBody {
            TODO("Show notifications on the notification bar by NotificationCompat")
        }

        private fun sendNotification(pushToken: String, sleepType: String) {
            TODO("Send the notification")
        }
    }
}