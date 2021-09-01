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
package com.hms.codelab.sleeptracker.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceHelper(
    private val context: Context,
) {
    private val sharedPreferences: SharedPreferences
        get() {
            return context.getSharedPreferences(Constants.packageName, Context.MODE_PRIVATE)
        }

    fun updateSleepTime(sleepTime: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.sleepTimeStr, sleepTime)
        editor.apply()
    }

    fun updateWakeUpTime(wakeUpTime: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.wakeUpTimeStr, wakeUpTime)
        editor.apply()
    }

    fun getSleepTime(): String? {
        return sharedPreferences.getString(Constants.sleepTimeStr, Constants.nanStr)
    }

    fun getWakeUpTime(): String? {
        return sharedPreferences.getString(Constants.wakeUpTimeStr, Constants.nanStr)
    }

    fun removeData() {
        sharedPreferences.edit().remove(Constants.sleepTimeStr).apply()
        sharedPreferences.edit().remove(Constants.wakeUpTimeStr).apply()
        sharedPreferences.edit().remove(Constants.alarmSwitchStateStr).apply()
        sharedPreferences.edit().remove(Constants.pushTokenStr).apply()
    }

    fun updateAlarmSwitchState(switchState: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(Constants.alarmSwitchStateStr, switchState)
        editor.apply()
    }

    fun getAlarmSwitchState(): Boolean {
        return sharedPreferences.getBoolean(Constants.alarmSwitchStateStr, false)
    }

    fun getPushToken(): String {
        return sharedPreferences.getString(Constants.pushTokenStr, Constants.nanStr)!!
    }

}