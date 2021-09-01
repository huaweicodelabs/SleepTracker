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

package com.hms.codelab.sleeptracker.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hms.codelab.sleeptracker.base.BaseViewModel;
import com.hms.codelab.sleeptracker.data.Data;
import com.hms.codelab.sleeptracker.data.Status;
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper;
import com.huawei.agconnect.auth.AGConnectAuth;

public class ProfileViewModel extends BaseViewModel {
    private SharedPreferenceHelper sharedPreferences;

    public ProfileViewModel(SharedPreferenceHelper sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private MutableLiveData<Data<String>> sleepTimeResult = new MutableLiveData<Data<String>>();
    public LiveData<Data<String >> sleepTimeResultLiveData = getSleepTimeResultLiveData();

    public LiveData<Data<String>> getSleepTimeResultLiveData() {
        return sleepTimeResult;
    }

    private MutableLiveData<Data<String>> wakeUpTimeResult = new MutableLiveData<Data<String>>();
    public LiveData<Data<String>> wakeUpTimeResultLiveData = getWakeUpTimeResultLiveData();

    public LiveData<Data<String>> getWakeUpTimeResultLiveData() {
        return wakeUpTimeResult;
    }

    public void getPreferencesData() {
        String sleepTime = sharedPreferences.getSleepTime();
        String wakeUpTime = sharedPreferences.getWakeUpTime();
        if (sleepTime.equals("NaN")) {
            sleepTime = "23:00";
            sharedPreferences.updateSleepTime(sleepTime);
        }
        sleepTimeResult.postValue(
                new Data(
                        Status.SUCCESSFUL,
                        sleepTime,
                        null
                )
        );
        if (wakeUpTime.equals("NaN")) {
            wakeUpTime = "07:00";
            sharedPreferences.updateWakeUpTime(wakeUpTime);
        }
        wakeUpTimeResult.postValue(
                new Data(
                        Status.SUCCESSFUL,
                        wakeUpTime,
                        null
                )
        );
    }

    public void logout() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().signOut();
            sharedPreferences.removeData();
        }
    }
}
