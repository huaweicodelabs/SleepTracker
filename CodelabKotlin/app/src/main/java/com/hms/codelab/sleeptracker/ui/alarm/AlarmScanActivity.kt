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
package com.hms.codelab.sleeptracker.ui.alarm
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.services.AlarmClock
import com.hms.codelab.sleeptracker.utils.Constants
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.ml.scan.HmsScan
import kotlinx.android.synthetic.main.fragment_qr_scanner.*

class AlarmScanActivity : AppCompatActivity() {

    private var remoteView: RemoteView? = null
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private val scanFrameSize = 300


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_scan)

        initScanKit(savedInstanceState)
//        scanningView.startAnimation()
    }

    override fun onStart() {
        super.onStart()
        remoteView?.onStart()

    }

    override fun onResume() {
        super.onResume()
        remoteView?.onResume()

    }

    override fun onPause() {
        super.onPause()
        remoteView?.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        remoteView?.onDestroy()

    }

    private fun initScanKit(savedInstanceState: Bundle?) {
        TODO("To obtain the QR Code, use RemoteView in the FrameLayout class of Scan Kit")
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //cameraSource.start(view)
            } else {
                Log.i(Constants.alarmScanActivityTAG, "onRequestPermission failed.")
            }
        }
    }

}