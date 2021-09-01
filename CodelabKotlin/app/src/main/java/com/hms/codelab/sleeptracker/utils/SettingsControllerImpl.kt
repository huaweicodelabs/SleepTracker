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
import com.huawei.hms.hihealth.HiHealthOptions
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.support.hwid.HuaweiIdAuthManager

class SettingsControllerImpl(val context: Context) {
    private val mSettingController: SettingController
        get() {
            val fitnessOptions = HiHealthOptions.builder().build()
            val signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(fitnessOptions)
            return HuaweiHiHealth.getSettingController(context, signInHuaweiId)
        }

    fun getSettingsController() : SettingController = mSettingController
}