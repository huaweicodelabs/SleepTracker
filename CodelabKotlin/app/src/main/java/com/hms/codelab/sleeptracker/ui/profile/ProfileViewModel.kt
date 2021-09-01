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
package com.hms.codelab.sleeptracker.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.codelab.sleeptracker.base.BaseViewModel
import com.hms.codelab.sleeptracker.data.Data
import com.hms.codelab.sleeptracker.data.Status
import com.huawei.agconnect.auth.AGConnectAuth
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper

class ProfileViewModel(
    private val sharedPreferences: SharedPreferenceHelper,
) : BaseViewModel() {

    private val sleepTimeResult = MutableLiveData<Data<String>>()
    val sleepTimeResultLiveData: LiveData<Data<String>>
        get() = sleepTimeResult

    private val wakeUpTimeResult = MutableLiveData<Data<String>>()
    val wakeUpTimeResultLiveData: LiveData<Data<String>>
        get() = wakeUpTimeResult

    fun getPreferencesData() {
        var sleepTime = sharedPreferences.getSleepTime()
        var wakeUpTime = sharedPreferences.getWakeUpTime()
        if (sleepTime == "NaN") {
            sleepTime = "23:00"
            sharedPreferences.updateSleepTime(sleepTime)
        }
        sleepTimeResult.postValue(
            Data(
                responseType = Status.SUCCESSFUL,
                data = sleepTime
            )
        )

        if (wakeUpTime == "NaN") {
            wakeUpTime = "07:00"
            sharedPreferences.updateWakeUpTime(wakeUpTime)
        }
        wakeUpTimeResult.postValue(
            Data(
                responseType = Status.SUCCESSFUL,
                data = wakeUpTime
            )
        )

    }

    fun logout() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            AGConnectAuth.getInstance().signOut()
            sharedPreferences.removeData()
        }
    }

}