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

package com.hms.codelab.sleeptracker.ui.mysleep.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.utils.Constants;

import java.util.Objects;

public class ExplanationDialogFragment extends DialogFragment {
    private String selected;

    public ExplanationDialogFragment(String selected) {
        this.selected = selected;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explanation_dialog, container, false);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        TextView refTitleTv = rootView.findViewById(R.id.refTitleTv);
        TextView explanationTv = rootView.findViewById(R.id.explanationTv);
        switch (selected) {
            case "totalSleep":
                refTitleTv.setText(Constants.totalSleepTitle);
                explanationTv.setText(Constants.totalSleepRef);
                break;
            case "deepSleep":
                refTitleTv.setText(Constants.deepSleepTitle);
                explanationTv.setText(Constants.deepSleepRef);
                break;
            case "lightSleep":
                refTitleTv.setText(Constants.lightSleepTitle);
                explanationTv.setText(Constants.lightSleepRef);
                break;
            case "remSleep":
                refTitleTv.setText(Constants.remSleepTitle);
                explanationTv.setText(Constants.remSleepRef);
                break;
            case "awakeCount":
                refTitleTv.setText(Constants.awakeCountTitle);
                explanationTv.setText(Constants.awakeCountRef);
                break;
        }
        return rootView;
    }
}
