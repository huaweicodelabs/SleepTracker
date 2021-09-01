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

package com.hms.codelab.sleeptracker.ui.login;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hms.codelab.sleeptracker.base.BaseViewModel;
import com.hms.codelab.sleeptracker.data.Data;
import com.hms.codelab.sleeptracker.data.SignInResult;
import com.hms.codelab.sleeptracker.data.Status;
import com.hms.codelab.sleeptracker.services.AccountService;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import kotlin.jvm.functions.Function1;

public class LoginViewModel extends BaseViewModel {
    private final AccountService accountService;

    public LoginViewModel(AccountService accountService) {
        this.accountService = accountService;
    }

    private MutableLiveData<Data<SignInResult>> huaweiSignInResult = new MutableLiveData<Data<SignInResult>>();
    public LiveData<Data<SignInResult>> huaweiSignInResultLiveData = getHuaweiSignInResultLiveData();

    public LiveData<Data<SignInResult>> getHuaweiSignInResultLiveData() {
        return huaweiSignInResult;
    }

    private MutableLiveData<Data<SignInResult>> transmitTokenResult = new MutableLiveData<Data<SignInResult>>();
    public LiveData<Data<SignInResult>> transmitTokenResultLiveData = getTransmitTokenResultLiveData();

    public LiveData<Data<SignInResult>> getTransmitTokenResultLiveData() {
        return transmitTokenResult;
    }

    public void getSignInIntent(Function1<Intent, Void> intent) {
        accountService.getSignInIntent(intent);
    }

    public void fetchHuaweiSignInResult(Intent intent) {
        huaweiSignInResult.postValue(new Data(Status.LOADING, null, null));
        DisposableSingleObserver<SignInResult> disposable = accountService.onSignInActivityResult(intent)
                .subscribeWith(new DisposableSingleObserver<SignInResult>() {
                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        huaweiSignInResult.postValue(new Data(Status.SUCCESSFUL, signInResult, null));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        huaweiSignInResult.postValue(new Data(Status.ERROR, null, new Error(e)));
                    }
                });
        addDisposable(disposable);
    }

    public void transmitTokenIntoAppGalleryConnect(String accessToken) {
        transmitTokenResult.postValue(new Data(Status.LOADING, null, null));
        DisposableSingleObserver<SignInResult> disposable = accountService.transmitTokenIntoAppGalleryConnect(accessToken)
                .subscribeWith(new DisposableSingleObserver<SignInResult>() {
                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        transmitTokenResult.postValue(new Data(Status.SUCCESSFUL, signInResult, null));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        transmitTokenResult.postValue(new Data(Status.ERROR, null, new Error(e)));
                    }
                });
        addDisposable(disposable);
    }

    public void queryHealthAuthorization() {
        accountService.queryHealthAuthorization();
    }

    public void checkOrAuthorizeHealth(Function1<Intent, Void> intent) {
        accountService.checkOrAuthorizeHealth(intent);
    }


}
