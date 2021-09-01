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

import com.hms.codelab.sleeptracker.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationInterface {
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("v1/${Constants.appId}/messages:send")
    fun createNotification(
        @Header("Authorization") authorization : String?,
        @Body notificationMessageBody : NotificationMessageBody
    ) : Call<NotificationMessage>
}