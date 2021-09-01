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
        val dm = resources.displayMetrics
        val density = dm.density
        mScreenWidth = resources.displayMetrics.widthPixels
        mScreenHeight = resources.displayMetrics.heightPixels

        val scanFrameSize = (scanFrameSize * density)
        val rect = Rect()
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()

        remoteView =
            RemoteView.Builder().setContext(this).setBoundingBox(rect)
                .setFormat(HmsScan.QRCODE_SCAN_TYPE).build()
        remoteView?.onCreate(savedInstanceState)
        remoteView?.setOnResultCallback { result -> //judge the result is effective

            println("QR CODE : " + result[0].getOriginalValue())
            if (result != null && result.isNotEmpty() && result[0] != null && !TextUtils.isEmpty(
                    result[0].getOriginalValue()
                ) && result[0].originalValue == Constants.alarmOffQRResult
            ) {
                val intentService = Intent(this, AlarmClock::class.java)
                stopService(intentService)
                finish()
            }
        }

        val params =
            FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

        rim.addView(remoteView, params)
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