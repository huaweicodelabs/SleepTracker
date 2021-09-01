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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.data.Status
import com.hms.codelab.sleeptracker.ui.MainActivity
import com.hms.codelab.sleeptracker.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.huaweiLoginBtn)
        loginBtn.setOnClickListener {
            loginViewModel.getSignInIntent {
                startActivityForResult(it, Constants.REQUEST_SIGN_IN_LOGIN)
            }
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        loginViewModel.huaweiSignInResultLiveData.observe(this, Observer {
            when (it.responseType) {
                Status.SUCCESSFUL -> {
                    Log.i(Constants.loginActivityTAG, getString(R.string.view_model_sign_success))
                    transmitTokenIntoAppGalleryConnect(it.data!!.token)

                }
                Status.ERROR -> {
                    Log.i(
                        Constants.loginActivityTAG,
                        "${getString(R.string.view_model_sign_failed)} ${it.error?.message}"
                    )
                }

                Status.LOADING -> {
                    Log.i(
                        Constants.loginActivityTAG,
                        getString(R.string.view_model_sign_failed)
                    )
                }
            }
        })

        loginViewModel.transmitTokenResultLiveData.observe(this, Observer {
            when (it.responseType) {
                Status.SUCCESSFUL -> {
                    Log.i(Constants.loginActivityTAG, getString(R.string.transmit_token_success))
                    loginViewModel.checkOrAuthorizeHealth { intent ->
                        startActivityForResult(intent, Constants.REQUEST_SIGN_IN_LOGIN)
                    }
                    loginViewModel.queryHealthAuthorization()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
                Status.ERROR -> {
                    Log.i(
                        Constants.loginActivityTAG,
                        "${getString(R.string.view_model_sign_failed)} ${it.error?.message}"
                    )
                }
                Status.LOADING -> {
                    Log.i(
                        Constants.loginActivityTAG,
                        getString(R.string.loading)
                    )
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataIntent)
        if (dataIntent != null && resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_SIGN_IN_LOGIN -> {
                    loginViewModel.fetchHuaweiSignInResult(dataIntent)
                }
            }

        }
    }

    private fun transmitTokenIntoAppGalleryConnect(accessToken: String) {
        loginViewModel.transmitTokenIntoAppGalleryConnect(accessToken)
    }

}