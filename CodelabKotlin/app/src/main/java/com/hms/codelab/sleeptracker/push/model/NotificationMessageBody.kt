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
package com.hms.codelab.sleeptracker.push.model


import com.google.gson.annotations.SerializedName
import com.hms.codelab.sleeptracker.utils.SerializedNames

class NotificationMessageBody(
    @field:SerializedName("validate_only") var validateOnly: Boolean,
    @field:SerializedName("message") var message: Message?
) {
    class Builder(
        private var title: String,
        private var body: String,
        private var pushToken: Array<String?>
    ) {
        fun build(): NotificationMessageBody {
            val clickAction =
                ClickAction(
                    3
                )
            val androidNotification =
                AndroidNotification(
                    title,
                    body,
                    clickAction
                )
            val androidConfig =
                AndroidConfig(
                    androidNotification
                )
            val notification =
                Notification(
                    title,
                    body
                )
            val message = Message(
                notification,
                androidConfig,
                pushToken
            )
            return NotificationMessageBody(
                false,
                message
            )
        }
    }

    class Message(
        @field:SerializedName(SerializedNames.notification) var notification: Notification,
        @field:SerializedName(SerializedNames.android) var android: AndroidConfig,
        @field:SerializedName(SerializedNames.token) var token: Array<String?>
    )

    class Notification(
        @field:SerializedName(SerializedNames.title) var title: String,
        @field:SerializedName(SerializedNames.body) var body: String
    )

    class AndroidConfig(
        @field:SerializedName(SerializedNames.notification) var notification: AndroidNotification
    )

    class AndroidNotification(
        @field:SerializedName(SerializedNames.title) var title: String,
        @field:SerializedName(SerializedNames.body) var body: String,
        @field:SerializedName(SerializedNames.clickAction) var clickAction: ClickAction
    )

    class ClickAction {
        @SerializedName(SerializedNames.type)
        var type: Int

        @SerializedName(SerializedNames.intent)
        var intent: String? = null

        constructor(type: Int) {
            this.type = type
        }

        constructor(type: Int, intent: String) {
            this.type = type
            this.intent = intent
        }
    }
}
