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

package com.hms.codelab.sleeptracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class SharedPreferenceHelper {
    private final Context context;

    public SharedPreferenceHelper(@NotNull Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences(Constants.packageName, Context.MODE_PRIVATE);
    }

    public void updateSleepTime(String sleepTime) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(Constants.sleepTimeStr, sleepTime);
        editor.apply();
    }

    public void updateWakeUpTime(String wakeUpTime) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(Constants.wakeUpTimeStr, wakeUpTime);
        editor.apply();
    }

    public String getSleepTime() {
        return getSharedPreferences().getString(Constants.sleepTimeStr, Constants.nanStr);
    }

    public String getWakeUpTime() {
        return getSharedPreferences().getString(Constants.wakeUpTimeStr, Constants.nanStr);
    }

    public void removeData() {
        getSharedPreferences().edit().remove(Constants.sleepTimeStr).apply();
        getSharedPreferences().edit().remove(Constants.wakeUpTimeStr).apply();
        getSharedPreferences().edit().remove(Constants.alarmSwitchStateStr).apply();
        getSharedPreferences().edit().remove(Constants.pushTokenStr).apply();
    }

    public void updateAlarmSwitchState(Boolean switchState) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(Constants.alarmSwitchStateStr, switchState);
        editor.apply();
    }

    public Boolean getAlarmSwitchState() {
        return getSharedPreferences().getBoolean(Constants.alarmSwitchStateStr, false);
    }

    public String getPushToken() {
        return getSharedPreferences().getString(Constants.pushTokenStr, Constants.nanStr);
    }
}
