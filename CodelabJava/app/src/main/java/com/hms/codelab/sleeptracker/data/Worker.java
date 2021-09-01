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

package com.hms.codelab.sleeptracker.data;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class Worker<T> {
    private Function1 successListener;
    private Function1 failureListener;
    private Function0 canceledListener;

    public final void onSuccess(Object result) {
        if (this.successListener != null) {
            this.successListener.invoke(result);
        }

    }

    public final void onFailure(Exception error) {
        if (this.failureListener != null) {
            this.failureListener.invoke(error);
        }

    }

    public final void onCanceled() {
        if (this.canceledListener != null) {
            this.onCanceled();
        }

    }
}
