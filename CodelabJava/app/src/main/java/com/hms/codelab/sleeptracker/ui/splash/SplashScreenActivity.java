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

package com.hms.codelab.sleeptracker.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.ui.MainActivity;
import com.hms.codelab.sleeptracker.ui.login.LoginActivity;
import com.huawei.agconnect.auth.AGConnectAuth;
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView splashText = findViewById(R.id.splashText);
        splashText.setAlpha(0f);
        splashText.animate().setDuration(2000).alpha(1f).withEndAction(
                new Runnable() {
                    @Override
                    public void run() {
                        if (AGConnectAuth.getInstance().getCurrentUser() == null) {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                }
        ).start();
    }
}
