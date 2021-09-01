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
package com.hms.codelab.sleeptracker.di

import com.hms.codelab.sleeptracker.ui.login.LoginViewModel
import com.hms.codelab.sleeptracker.ui.mysleep.MySleepViewModel
import com.hms.codelab.sleeptracker.ui.profile.ProfileViewModel
import com.hms.codelab.sleeptracker.utils.DataControllerImpl
import com.hms.codelab.sleeptracker.utils.HuaweiAccountServiceImpl
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mViewModelModules = module {

    viewModel {
        ProfileViewModel(
            sharedPreferences = SharedPreferenceHelper(
                context = androidContext(),
            )
        )
    }

    viewModel {
        MySleepViewModel(
            accountService = HuaweiAccountServiceImpl(
                androidContext(),
                dataController = DataControllerImpl(androidContext()).dataController,
                mSettingController = get()
            )
        )
    }

    viewModel {
        LoginViewModel(
            accountService = HuaweiAccountServiceImpl(
                androidContext(),
                dataController = get(),
                mSettingController = get()
            )
        )
    }

}