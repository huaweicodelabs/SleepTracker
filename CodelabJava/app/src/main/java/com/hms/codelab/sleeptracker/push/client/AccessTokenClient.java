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

package com.hms.codelab.sleeptracker.push.client;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccessTokenClient {
    private static AccessTokenClient instance;

    private AccessTokenClient() {

    }

    public static AccessTokenClient getInstance() {
        if (instance == null) {
            instance = new AccessTokenClient();
        }
        return instance;
    }

    private static final String accessTokenBaseURL = "https://oauth-login.cloud.huawei.com/";
    private static final long TIMEOUT = 500000L;

    public static Retrofit getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MICROSECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor).build();
        return new Retrofit.Builder()
                .baseUrl(accessTokenBaseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

}
