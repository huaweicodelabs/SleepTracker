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
package com.hms.codelab.sleeptracker.ui.mysleep

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.codelab.sleeptracker.base.BaseViewModel
import com.hms.codelab.sleeptracker.data.Data
import com.hms.codelab.sleeptracker.data.Status
import com.hms.codelab.sleeptracker.services.AccountService
import com.huawei.hms.hihealth.data.SampleSet
import com.hms.codelab.sleeptracker.data.SignInResult
import com.hms.codelab.sleeptracker.utils.Constants
import io.reactivex.observers.DisposableSingleObserver

class MySleepViewModel(
    private val accountService: AccountService
) : BaseViewModel() {

    private val dailyDataResult = MutableLiveData<Data<SampleSet>>()
    val dailyDataResultLiveData: LiveData<Data<SampleSet>>
        get() = dailyDataResult

    fun readToday() {
        val disposable = accountService.readToday()
            .subscribeWith(object : DisposableSingleObserver<SampleSet>() {
                override fun onSuccess(t: SampleSet) {
                    dailyDataResult.postValue(
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = t
                        )
                    )
                }

                override fun onError(e: Throwable) {
                    dailyDataResult.postValue(
                        Data(
                            responseType = Status.ERROR,
                            error = Error(e)
                        )
                    )
                }

            })

        addDisposable(disposable)
    }

    fun readDailyData(startTime: Int, endTime: Int) {
        val disposable = accountService.readDailyData(startTime, endTime)
            .subscribeWith(object : DisposableSingleObserver<SampleSet>() {
                override fun onSuccess(t: SampleSet) {
                    dailyDataResult.postValue(
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = t
                        )
                    )
                }

                override fun onError(e: Throwable) {
                    dailyDataResult.postValue(
                        Data(
                            responseType = Status.ERROR,
                            error = Error(e)
                        )
                    )
                }

            })

        addDisposable(disposable)
    }

    fun silentSignIn() {
        val disposable = accountService.silentSignIn()
            .subscribeWith(object : DisposableSingleObserver<SignInResult>() {
                override fun onSuccess(t: SignInResult) {
                    Log.i(Constants.silentSignInTAG, "Silent Sign In success")
                }

                override fun onError(e: Throwable) {
                    Log.i(Constants.silentSignInTAG, "Silent Sign In failed.")
                }

            })

        addDisposable(disposable)
    }
}