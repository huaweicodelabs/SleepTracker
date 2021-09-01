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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.ui.MainActivity;
import com.hms.codelab.sleeptracker.utils.Constants;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;

public class LoginActivity extends AppCompatActivity {
    private final Lazy<LoginViewModel> loginViewModel = inject(LoginViewModel.class);
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            loginViewModel.getValue().fetchHuaweiSignInResult(data);
                        }
                    }
                });

        Button loginBtn = findViewById(R.id.huaweiLoginBtn);
        loginBtn.setOnClickListener(view -> {
            loginViewModel.getValue().getSignInIntent(intent -> {
                someActivityResultLauncher.launch(intent);
                //TODO check this return
                return null;
            });
        });

        observeLiveData();
    }

    private void observeLiveData() {
        loginViewModel.getValue().huaweiSignInResultLiveData.observe(this, it -> {
            switch (it.getResponseType()) {
                case SUCCESSFUL:
                    Log.i(Constants.loginActivityTAG, getString(R.string.view_model_sign_success));
                    if (it.getData() != null) {
                        transmitTokenIntoAppGalleryConnect(it.getData().getToken());
                    }
                    break;
                case LOADING:
                    Log.i(
                            Constants.loginActivityTAG,
                            getString(R.string.view_model_sign_failed)
                    );
                    break;
                case ERROR:
                    Log.i(
                            Constants.loginActivityTAG,
                            getString(R.string.view_model_sign_failed) + " " + it.getError().getMessage()
                    );
                    break;
            }
        });

        loginViewModel.getValue().transmitTokenResultLiveData.observe(this, it -> {

            switch (it.getResponseType()) {
                case SUCCESSFUL:
                    Log.i(Constants.loginActivityTAG, getString(R.string.transmit_token_success));
                    loginViewModel.getValue().checkOrAuthorizeHealth(intent -> {
                        someActivityResultLauncher.launch(intent);
                        //TODO check this return
                        return null;
                    });
                    loginViewModel.getValue().queryHealthAuthorization();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                case LOADING:
                    Log.i(
                            Constants.loginActivityTAG,
                            getString(R.string.loading)
                    );
                    break;
                case ERROR:
                    if (it.getError() != null) {
                        Log.i(
                                Constants.loginActivityTAG,
                                getString(R.string.view_model_sign_failed) + " " + it.getError().getMessage()
                        );
                    }
                    break;
            }

        });
    }

    private void transmitTokenIntoAppGalleryConnect(String accessToken) {
        loginViewModel.getValue().transmitTokenIntoAppGalleryConnect(accessToken);
    }


}
