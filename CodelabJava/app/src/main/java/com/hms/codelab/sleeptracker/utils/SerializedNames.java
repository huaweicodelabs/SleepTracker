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

import org.jetbrains.annotations.NotNull;

public class SerializedNames {
    @NotNull
    public static final String android = "android";
    @NotNull
    public static final String title = "title";
    @NotNull
    public static final String notification = "notification";
    @NotNull
    public static final String token = "token";
    @NotNull
    public static final String body = "body";
    @NotNull
    public static final String clickAction = "click_action";
    @NotNull
    public static final String type = "type";
    @NotNull
    public static final String intent = "intent";
    @NotNull
    public static final SerializedNames INSTANCE;

    private SerializedNames() {
    }

    static {
        SerializedNames var0 = new SerializedNames();
        INSTANCE = var0;
    }
}
