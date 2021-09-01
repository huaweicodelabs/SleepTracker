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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.data.SignInResult;
import com.hms.codelab.sleeptracker.data.Worker;
import com.hms.codelab.sleeptracker.services.AccountService;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hihealth.DataController;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.hihealth.data.DataType;
import com.huawei.hms.hihealth.data.SampleSet;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import kotlin.jvm.functions.Function1;

public class HuaweiAccountServiceImpl implements AccountService {
    private HuaweiIdAuthService mHuaweiIdAuthService;
    private final HuaweiAccountMapper mapper;
    public static AuthHuaweiId user;
    @NotNull
    private final Context context;
    @NotNull
    private final DataController dataController;
    private final SettingController mSettingController;

    @Nullable
    public final AuthHuaweiId getUser() {
        return HuaweiAccountServiceImpl.user;
    }

    public final void setUser(@Nullable AuthHuaweiId var1) {
        HuaweiAccountServiceImpl.user = var1;
    }


    public HuaweiAccountServiceImpl(@NotNull Context context, @NotNull DataController dataController, SettingController mSettingController) {
        this.context = context;
        this.dataController = dataController;
        this.mSettingController = mSettingController;
        this.mapper = new HuaweiAccountMapper();
        // TODO create a new HuaweiIdAuthParams and set this variables.
        this.mHuaweiIdAuthService = HuaweiIdAuthManager.getService(this.context, params);
    }

    private List<Scope> getScopes() {
        // TODO Create a scope list and return it with  Sleep Read and Base Scope
    }

    @Override
    public Single<SignInResult> silentSignIn() {
        return Single.create(emitter -> {
            Task<AuthHuaweiId> task = mHuaweiIdAuthService.silentSignIn();
            task.addOnSuccessListener(authHuaweiId -> {
                emitter.onSuccess(mapper.map(authHuaweiId));
                setUser(authHuaweiId);
            });
            task.addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Single<SignInResult> transmitTokenIntoAppGalleryConnect(String accessToken) {
        return Single.create(emitter -> {
            AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken);
            AGConnectAuth.getInstance().signIn(credential)
                    .addOnFailureListener(e -> {
                        Log.e(Constants.loginActivityTAG, "Transmission Error: " + e.getMessage());
                    })
                    .addOnSuccessListener(signInResult -> {
                        //TODO maybe Set.of() instead of HashSet()
                        emitter.onSuccess(new SignInResult(signInResult.getUser().getUid(), new HashSet()));
                    });
        });
    }

    @Override
    public void getSignInIntent(Function1<Intent, Void> intent) {
        intent.invoke(mHuaweiIdAuthService.getSignInIntent());
    }

    @Override
    public Single<SignInResult> onSignInActivityResult(Intent intent) {
        return Single.create(emitter -> {
            Task<AuthHuaweiId> task = mHuaweiIdAuthService.silentSignIn();
            task.addOnSuccessListener(authHuaweiId -> {
                Log.i("HuaweiAccountServiceImpl", authHuaweiId.getEmail());
                emitter.onSuccess(mapper.map(authHuaweiId));
                setUser(authHuaweiId);
            });
            task.addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Worker<Void> signOut() {
        Worker<Void> worker = new Worker<Void>();

        mHuaweiIdAuthService.signOut()
                .addOnSuccessListener(worker::onSuccess)
                .addOnFailureListener(worker::onFailure)
                .addOnCanceledListener(worker::onCanceled);
        return worker;
    }

    @Override
    public Single<SampleSet> readToday() {
        return Single.create(emitter -> {
            // TODO Create a today summary task as SampleSet with data controller's read today summation for continuous sleep
            // TODO Add onSuccessListener and onFailureListener to this task.
        });
    }

    @Override
    public Single<SampleSet> readDailyData(Integer startTime, Integer endTime) {
        return Single.create(emitter -> {
            // TODO Create a daily summary task as SampleSet with data controller's read daily summation for continuous sleep with start and end time
            // TODO Add onSuccessListener and onFailureListener to this task.
        });
    }

    @Override
    public void queryHealthAuthorization() {
        Task<Boolean> queryTask = mSettingController.getHealthAppAuthorization();
        queryTask.addOnSuccessListener(aBoolean -> {
            if (Boolean.TRUE.equals(aBoolean)) {
                Log.i(
                        Constants.loginActivityTAG,
                        context.getString(R.string.query_authorized)
                );
            } else {
                Log.i(
                        Constants.loginActivityTAG,
                        context.getString(R.string.query_not_authorized)
                );
            }
        });
        queryTask.addOnFailureListener(e -> {
            if (e != null) {
                Log.i(Constants.loginActivityTAG, context.getString(R.string.query_exception));
                Log.i(Constants.loginActivityTAG, e.getMessage());
            }
        });
    }

    @Override
    public void checkOrAuthorizeHealth(Function1<Intent, Void> intent) {
        String healthAppSettingDataShareHealthKitActivityScheme =
                "huaweischeme://healthapp/achievement?module=kit";
        // TODO Create an auth task with onSuccessListener and onFailureListener. Open Health intent on success
    }
}
