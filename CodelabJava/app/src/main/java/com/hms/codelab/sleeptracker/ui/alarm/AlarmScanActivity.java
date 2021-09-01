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

package com.hms.codelab.sleeptracker.ui.alarm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.services.AlarmClock;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;

public class AlarmScanActivity extends AppCompatActivity {
    @Nullable
    private RemoteView remoteView = null;
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private int scanFrameSize = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_scan);

        initScanKit(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        assert remoteView != null;
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        assert remoteView != null;
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        assert remoteView != null;
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        assert remoteView != null;
        remoteView.onDestroy();
    }

    private void initScanKit(@Nullable Bundle savedInstanceState) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        float scanFrameSize = (this.scanFrameSize * density);
        Rect rect = new Rect();
        rect.left = (int) (((double)mScreenWidth / 2) - (scanFrameSize / 2));
        rect.left = (int) (((double)mScreenWidth / 2) - (scanFrameSize / 2));
        rect.right = (int) ((double)mScreenWidth / 2 + scanFrameSize / 2);
        rect.top = (int) ((double)mScreenHeight / 2 - scanFrameSize / 2);
        rect.bottom = (int) ((double)mScreenHeight / 2 + scanFrameSize / 2);

        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect)
                .setFormat(HmsScan.QRCODE_SCAN_TYPE).build();
        remoteView.onCreate(savedInstanceState);
        remoteView.setOnResultCallback(result -> {
            System.out.println("QR Code: " + result[0].getOriginalValue());
            if (result[0] != null && !TextUtils.isEmpty(
                    result[0].getOriginalValue()
            ) && result[0].originalValue.equals(Constants.alarmOffQRResult)
            ) {
                Intent intentService = new Intent(this, AlarmClock.class);
                stopService(intentService);
                finish();
            }
        });

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

        FrameLayout rim = findViewById(R.id.rim);
        rim.addView(remoteView, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //cameraSource.start(view);
            } else {
                Log.i(Constants.alarmScanActivityTAG, "onRequestPermission failed.");
            }
        }
    }
}
