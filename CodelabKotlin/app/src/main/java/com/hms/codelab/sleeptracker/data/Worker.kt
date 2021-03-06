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
package com.hms.codelab.sleeptracker.data

class Worker<T> {
    private var successListener: ((result: T) -> Unit)? = null
    private var failureListener: ((error: Exception) -> Unit)? = null
    private var canceledListener: (() -> Unit)? = null

    internal fun onSuccess(result: T) {
        successListener?.run { this(result!!) }
    }

    internal fun onFailure(error: Exception) {
        failureListener?.run { this(error) }
    }

    internal fun onCanceled() {
        canceledListener?.run { this() }
    }
}