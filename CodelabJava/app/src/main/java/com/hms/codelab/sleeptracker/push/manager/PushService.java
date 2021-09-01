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

import com.hms.codelab.sleeptracker.utils.Constants;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class PushService extends HmsMessageService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (!TextUtils.isEmpty(token)) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(
                    Constants.packageName,
                    Context.MODE_PRIVATE
            );
            sharedPreferences.edit().putString("pushToken", token).apply();
        }
        Log.i(Constants.pushServiceTAG, "Receive Token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        if (message != null) {
            Log.i(
                    Constants.pushServiceTAG, "getCollapseKey: " + message.getCollapseKey()
                            + "\n getData: " + message.getData()
                            + "\n getFrom: " + message.getFrom()
                            + "\n getTo: " + message.getTo()
                            + "\n getMessageId: " + message.getMessageId()
                            + "\n getSendTime: " + message.getSentTime()
                            + "\n getMessageType: " + message.getMessageType()
                            + "\n getTtl: " + message.getTtl());
        }
    }
}
