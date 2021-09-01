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

import android.text.TextUtils
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import com.hms.codelab.sleeptracker.utils.Constants
import java.util.*

class PushService : HmsMessageService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        if (!TextUtils.isEmpty(token)) {
            val sharedPreferences = this.getSharedPreferences(
                Constants.packageName,
                android.content.Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putString("pushToken", token).apply()
        }
        Log.i(Constants.pushServiceTAG, "Receive Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.i(
            Constants.pushServiceTAG, "getCollapseKey: " + message?.collapseKey
                    + "\n getData: " + message?.data
                    + "\n getFrom: " + message?.from
                    + "\n getTo: " + message?.to
                    + "\n getMessageId: " + message?.messageId
                    + "\n getSendTime: " + message?.sentTime
                    + "\n getMessageType: " + message?.messageType
                    + "\n getTtl: " + message?.ttl
        )

        val notification: RemoteMessage.Notification = message!!.notification

        Log.i(
            Constants.pushServiceTAG, "\n getImageUrl: " + notification.imageUrl
                    + "\n getTitle: " + notification.title
                    + "\n getTitleLocalizationKey: " + notification.titleLocalizationKey
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.titleLocalizationArgs)
                    + "\n getBody: " + notification.body
                    + "\n getBodyLocalizationKey: " + notification.bodyLocalizationKey
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.bodyLocalizationArgs)
                    + "\n getIcon: " + notification.icon
                    + "\n getSound: " + notification.sound
                    + "\n getTag: " + notification.tag
                    + "\n getColor: " + notification.color
                    + "\n getClickAction: " + notification.clickAction
                    + "\n getChannelId: " + notification.channelId
                    + "\n getLink: " + notification.link
                    + "\n getNotifyId: " + notification.notifyId
        )
    }
}