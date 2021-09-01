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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Data<RequestData> {
    private Status responseType;
    @Nullable
    private RequestData data;
    @Nullable
    private Error error;

    public final Status getResponseType() {
        return this.responseType;
    }

    public final void setResponseType(Status status) {
        this.responseType = status;
    }

    @Nullable
    public final RequestData getData() {
        return this.data;
    }

    public final void setData(@Nullable RequestData data) {
        this.data = data;
    }

    @Nullable
    public final Error getError() {
        return this.error;
    }

    public final void setError(@Nullable Error error) {
        this.error = error;
    }

    public Data(Status responseType, @Nullable RequestData data, @Nullable Error error) {
        super();
        this.responseType = responseType;
        this.data = data;
        this.error = error;
    }

    public final Data copy(Status responseType, RequestData data, Error error) {
        return new Data(responseType, data, error);
    }

    @NotNull
    public String toString() {
        return "Data(responseType=" + this.responseType + ", data=" + this.data + ", error=" + this.error + ")";
    }

}