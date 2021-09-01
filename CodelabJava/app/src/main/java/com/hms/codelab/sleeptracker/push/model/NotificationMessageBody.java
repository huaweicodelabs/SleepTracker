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

package com.hms.codelab.sleeptracker.push.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hms.codelab.sleeptracker.utils.SerializedNames;

import java.util.List;

public class NotificationMessageBody {
    @SerializedName("validate_only")
    private final boolean validateOnly;
    @SerializedName("message")
    @Nullable
    private final Message message;

    public NotificationMessageBody(boolean validateOnly, @Nullable Message message) {
        this.validateOnly = validateOnly;
        this.message = message;
    }

    public static class Builder {
        public String title;
        public String body;
        public List<String> pushToken;

        public Builder(String title, String body, List<String> pushToken) {
            this.title = title;
            this.body = body;
            this.pushToken = pushToken;
        }

        public NotificationMessageBody build() {
            ClickAction clickAction = new ClickAction(3);
            AndroidNotification androidNotification = new AndroidNotification(title, body, clickAction);
            AndroidConfig androidConfig = new AndroidConfig(androidNotification);
            Notification notification = new Notification(title, body);
            Message message = new Message(notification, androidConfig, pushToken);
            return new NotificationMessageBody(false, message);
        }

    }

    static class Message {
        @SerializedName(SerializedNames.notification)
        Notification notification;
        @SerializedName(SerializedNames.android)
        AndroidConfig android;
        @SerializedName(SerializedNames.token)
        List<String> token;

        public Message(Notification notification, AndroidConfig android, List<String> token) {
            this.notification = notification;
            this.android = android;
            this.token = token;
        }
    }

    static class Notification {
        @SerializedName(SerializedNames.title)
        String title;
        @SerializedName(SerializedNames.body)
        String body;

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    static class AndroidConfig {
        @SerializedName(SerializedNames.notification)
        AndroidNotification notification;

        public AndroidConfig(AndroidNotification notification) {
            this.notification = notification;
        }
    }


    static class AndroidNotification {
        @SerializedName(SerializedNames.title)
        String title;
        @SerializedName(SerializedNames.body)
        String body;
        @SerializedName(SerializedNames.clickAction)
        ClickAction clickAction;

        public AndroidNotification(String title, String body, ClickAction clickAction) {
            this.title = title;
            this.body = body;
            this.clickAction = clickAction;
        }
    }

    static class ClickAction {
        @SerializedName(SerializedNames.type)
        Integer type;

        @SerializedName(SerializedNames.intent)
        String intent = null;

        ClickAction(Integer type) {
            this.type = type;
        }

        ClickAction(Integer type, String intent) {
            this.type = type;
            this.intent = intent;
        }
    }
}
