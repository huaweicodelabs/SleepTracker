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

package com.hms.codelab.sleeptracker.ui.mysleep.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.utils.Constants
import kotlinx.android.synthetic.main.fragment_explanation_dialog.view.*

class ExplanationDialogFragment(private val selected: String): DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_explanation_dialog, container, false)
        dialog!!.setCanceledOnTouchOutside(true)
        when (selected) {
            Constants.DialogType.TOTAL.sleepType -> {
                rootView.refTitleTv.text = Constants.totalSleepTitle
                rootView.explanationTv.text = Constants.totalSleepRef
            }
            Constants.DialogType.DEEP.sleepType -> {
                rootView.refTitleTv.text = Constants.deepSleepTitle
                rootView.explanationTv.text = Constants.deepSleepRef
            }
            Constants.DialogType.LIGHT.sleepType -> {
                rootView.refTitleTv.text = Constants.lightSleepTitle
                rootView.explanationTv.text = Constants.lightSleepRef
            }
            Constants.DialogType.REM.sleepType -> {
                rootView.refTitleTv.text = Constants.remSleepTitle
                rootView.explanationTv.text = Constants.remSleepRef
            }
            Constants.DialogType.AWAKE.sleepType -> {
                rootView.refTitleTv.text = Constants.awakeCountTitle
                rootView.explanationTv.text = Constants.awakeCountRef
            }
        }
        return rootView
    }
}