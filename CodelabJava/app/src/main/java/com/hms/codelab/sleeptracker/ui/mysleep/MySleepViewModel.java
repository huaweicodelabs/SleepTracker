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

package com.hms.codelab.sleeptracker.ui.mysleep;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hms.codelab.sleeptracker.base.BaseViewModel;
import com.hms.codelab.sleeptracker.data.Data;
import com.hms.codelab.sleeptracker.data.SignInResult;
import com.hms.codelab.sleeptracker.data.Status;
import com.hms.codelab.sleeptracker.services.AccountService;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.huawei.hms.hihealth.data.SampleSet;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

public class MySleepViewModel extends BaseViewModel {
    private AccountService accountService;

    public MySleepViewModel(AccountService accountService) {
        this.accountService = accountService;
    }

    private MutableLiveData<Data<SampleSet>> dailyDataResult = new MutableLiveData<>();
    public LiveData<Data<SampleSet>> dailyDataResultLiveData = getDailyDataResultLiveData();

    public LiveData<Data<SampleSet>> getDailyDataResultLiveData() {
        return dailyDataResult;
    }

    public void readToday() {
        DisposableSingleObserver<SampleSet> disposable = accountService.readToday()
                .subscribeWith(new DisposableSingleObserver<SampleSet>() {
                    @Override
                    public void onSuccess(@NonNull SampleSet sampleSet) {
                        dailyDataResult.postValue(
                                new Data(
                                        Status.SUCCESSFUL,
                                        sampleSet,
                                        null
                                )
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Data(
                                Status.ERROR,
                                new Error(e),
                                null
                        );
                    }
                });
        addDisposable(disposable);
    }

    public void readDailyData(int startTime, int endTime) {
        DisposableSingleObserver<SampleSet> disposable = accountService
                .readDailyData(startTime, endTime)
                .subscribeWith(new DisposableSingleObserver<SampleSet>() {
                    @Override
                    public void onSuccess(@NonNull SampleSet sampleSet) {
                        dailyDataResult.postValue(
                                new Data(
                                        Status.SUCCESSFUL,
                                        sampleSet,
                                        null
                                )
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Data(
                                Status.ERROR,
                                new Error(e),
                                null
                        );
                    }
                });
        addDisposable(disposable);
    }

    public void silentSignIn() {
        DisposableSingleObserver<SignInResult> disposable = accountService.silentSignIn()
                .subscribeWith(new DisposableSingleObserver<SignInResult>() {
                    @Override
                    public void onSuccess(@NonNull SignInResult signInResult) {
                        Log.i(Constants.silentSignInTAG, "Silent Sign In success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(Constants.silentSignInTAG, "Silent Sign In failed.");
                    }
                });
        addDisposable(disposable);
    }
}
