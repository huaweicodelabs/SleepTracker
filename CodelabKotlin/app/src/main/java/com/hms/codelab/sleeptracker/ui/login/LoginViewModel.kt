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
package com.hms.codelab.sleeptracker.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hms.codelab.sleeptracker.base.BaseViewModel
import com.hms.codelab.sleeptracker.data.Data
import com.hms.codelab.sleeptracker.data.Status
import com.hms.codelab.sleeptracker.services.AccountService
import com.hms.codelab.sleeptracker.data.SignInResult
import io.reactivex.observers.DisposableSingleObserver

class LoginViewModel(
    private val accountService: AccountService
) : BaseViewModel() {

    private val huaweiSignInResult = MutableLiveData<Data<SignInResult>>()
    val huaweiSignInResultLiveData: LiveData<Data<SignInResult>>
        get() = huaweiSignInResult

    private val transmitTokenResult = MutableLiveData<Data<SignInResult>>()
    val transmitTokenResultLiveData: LiveData<Data<SignInResult>>
        get() = transmitTokenResult

    fun getSignInIntent(intent: (Intent) -> Unit) {
        accountService.getSignInIntent(intent)
    }

    fun fetchHuaweiSignInResult(intent: Intent) {
        huaweiSignInResult.postValue(
            Data(
                responseType = Status.LOADING
            )
        )
        val disposable = accountService.onSignInActivityResult(intent)
            .subscribeWith(object : DisposableSingleObserver<SignInResult>() {
                override fun onSuccess(t: SignInResult) {
                    huaweiSignInResult.postValue(
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = t
                        )
                    )
                }

                override fun onError(e: Throwable) {
                    huaweiSignInResult.postValue(
                        Data(
                            responseType = Status.ERROR,
                            error = Error(e)
                        )
                    )
                }

            })

        addDisposable(disposable)
    }

    fun transmitTokenIntoAppGalleryConnect(accessToken: String) {
        transmitTokenResult.postValue(
            Data(
                responseType = Status.LOADING
            )
        )
        val disposable = accountService.transmitTokenIntoAppGalleryConnect(accessToken)
            .subscribeWith(object : DisposableSingleObserver<SignInResult>() {
                override fun onSuccess(t: SignInResult) {
                    transmitTokenResult.postValue(
                        Data(
                            responseType = Status.SUCCESSFUL,
                            data = t
                        )
                    )
                }

                override fun onError(e: Throwable) {
                    transmitTokenResult.postValue(
                        Data(
                            responseType = Status.ERROR,
                            error = Error(e)
                        )
                    )
                }

            })

        addDisposable(disposable)
    }

    fun queryHealthAuthorization() {
        accountService.queryHealthAuthorization()
    }

    fun checkOrAuthorizeHealth(intent: (Intent) -> Unit) {
        accountService.checkOrAuthorizeHealth(intent)
    }

}