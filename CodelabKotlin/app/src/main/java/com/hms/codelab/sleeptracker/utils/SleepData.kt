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

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SleepData(
    var sleepDate: String,
    var totalSleepTime: Int,
    var fallAsleepTime: Long,
    var wakeUpTime: Long,
    var lightSleepTime: Int,
    var deepSleepTime: Int,
    var remSleepTime: Int,
    var wakeUpCount: Int,
    var sleepScore: Int
) : Parcelable