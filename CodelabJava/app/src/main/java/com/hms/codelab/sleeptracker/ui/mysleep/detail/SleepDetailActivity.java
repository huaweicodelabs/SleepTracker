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

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.hms.codelab.sleeptracker.utils.SleepData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SleepDetailActivity extends AppCompatActivity {
    @Nullable
    private SleepData sleepDataObj = null;
    private PieChart sleepDetailChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_detail);
        sleepDetailChart = findViewById(R.id.sleepDetailChart);
        sleepDataObj = getIntent().getParcelableExtra(Constants.sleepDataStr);
        setChartDisplay();
        setTextViews();
        CardView totalSleepCardView = findViewById(R.id.totalSleepCardView);
        totalSleepCardView.setOnClickListener(view -> {
            ExplanationDialogFragment dialog = new ExplanationDialogFragment(Constants.DialogType.TOTAL.sleepType);
            dialog.show(getSupportFragmentManager(), Constants.explanationFragmentTAG);
        });
        CardView deepSleepCardView = findViewById(R.id.deepSleepCardView);
        deepSleepCardView.setOnClickListener(view -> {
            ExplanationDialogFragment dialog = new ExplanationDialogFragment(Constants.DialogType.DEEP.sleepType);
            dialog.show(getSupportFragmentManager(), Constants.explanationFragmentTAG);
        });
        CardView lightSleepCardView = findViewById(R.id.lightSleepCardView);
        lightSleepCardView.setOnClickListener(view -> {
            ExplanationDialogFragment dialog = new ExplanationDialogFragment(Constants.DialogType.LIGHT.sleepType);
            dialog.show(getSupportFragmentManager(), Constants.explanationFragmentTAG);
        });
        CardView remSleepCardView = findViewById(R.id.remSleepCardView);
        remSleepCardView.setOnClickListener(view -> {
            ExplanationDialogFragment dialog = new ExplanationDialogFragment(Constants.DialogType.REM.sleepType);
            dialog.show(getSupportFragmentManager(), Constants.explanationFragmentTAG);
        });
        CardView awakeCountCardView = findViewById(R.id.awakeCountCardView);
        awakeCountCardView.setOnClickListener(view -> {
            ExplanationDialogFragment dialog = new ExplanationDialogFragment(Constants.DialogType.AWAKE.sleepType);
            dialog.show(getSupportFragmentManager(), Constants.explanationFragmentTAG);
        });
    }

    private void setTextViews() {
        assert sleepDataObj != null;
        TextView sleepTimeDetailTv = findViewById(R.id.sleepTimeDetailTv);
        TextView sleepScoreValDetailTv = findViewById(R.id.sleepScoreValDetailTv);
        String sleepTimeText = "You slept " + intToHour(sleepDataObj.totalSleepTime);
        sleepTimeDetailTv.setText(sleepTimeText);
        sleepScoreValDetailTv.setText(String.valueOf(sleepDataObj.sleepScore));
        TextView totalSleepTv = findViewById(R.id.totalSleepTv);
        String totalSleepTimeText = Constants.totalSleepTitle + ": " + intToHour(sleepDataObj.totalSleepTime);
        totalSleepTv.setText(totalSleepTimeText);
        if (sleepDataObj.totalSleepTime < 360) {
            totalSleepTv.setTextColor(Constants.blue);
        } else if (sleepDataObj.totalSleepTime > 600) {
            totalSleepTv.setTextColor(Constants.red);
        }
        int deepSleepPercentage = percentageCalculate(
                sleepDataObj.totalSleepTime,
                sleepDataObj.deepSleepTime
        );
        TextView deepSleepDetailTv = findViewById(R.id.deepSleepDetailTv);
        String deepSleepTimeText = Constants.deepSleepTitle + ": " + deepSleepPercentage + " %";
        deepSleepDetailTv.setText(deepSleepTimeText);
        if (deepSleepPercentage < 20) {
            deepSleepDetailTv.setTextColor(Constants.blue);
        } else if (deepSleepPercentage > 60) {
            deepSleepDetailTv.setTextColor(Constants.red);
        }
        int lightSleepPercentage = percentageCalculate(
                sleepDataObj.totalSleepTime,
                sleepDataObj.lightSleepTime
        );
        TextView lightSleepDetailTv = findViewById(R.id.lightSleepDetailTv);
        String lightSleepTimeText = Constants.lightSleepTitle + ": " + lightSleepPercentage + " %";
        lightSleepDetailTv.setText(lightSleepTimeText);
        if (lightSleepPercentage >= 55) {
            lightSleepDetailTv.setTextColor(Constants.red);
        }
        int remSleepPercentage = percentageCalculate(
                sleepDataObj.totalSleepTime,
                sleepDataObj.remSleepTime
        );
        TextView remSleepDetailTv = findViewById(R.id.remSleepDetailTv);
        String  remSleepTimeText = Constants.remSleepTitle + ": " + remSleepPercentage + " %";
        remSleepDetailTv.setText(remSleepTimeText);
        if (remSleepPercentage < 10) {
            remSleepDetailTv.setTextColor(Constants.blue);
        } else if (remSleepPercentage > 30) {
            remSleepDetailTv.setTextColor(Constants.red);
        }
        TextView awakeDetailTv = findViewById(R.id.awakeDetailTv);
        String awakeCountText = Constants.awakeCountTitle + ": " + sleepDataObj.wakeUpCount + " times";
        awakeDetailTv.setText(awakeCountText);
        if (sleepDataObj.wakeUpCount > 2){
            awakeDetailTv.setTextColor(Constants.red);
        }
        TextView bedTimeValTv = findViewById(R.id.bedTimeValTv);
        TextView riseTimeValTv = findViewById(R.id.riseTimeValTv);
        bedTimeValTv.setText(longToHour(sleepDataObj.fallAsleepTime));
        riseTimeValTv.setText(longToHour(sleepDataObj.wakeUpTime));
    }

    private void setChartData() {
        ArrayList<PieEntry> dailySleepInfo = new ArrayList<PieEntry>();
        if (sleepDataObj != null) {
            dailySleepInfo.add(
                    new PieEntry(
                            (float) sleepDataObj.lightSleepTime,
                            Constants.lightSleepTitle
                    )
            );
            dailySleepInfo.add(
                    new PieEntry(
                            (float) sleepDataObj.deepSleepTime,
                            Constants.deepSleepTitle
                    )
            );
            dailySleepInfo.add(
                    new PieEntry(
                            (float) sleepDataObj.remSleepTime,
                            Constants.remSleepTitle
                    )
            );
        }
        PieDataSet pieDataSet = new PieDataSet(dailySleepInfo, "");
        int[] colorList = new int[]{Color.rgb(148, 0, 211), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0)};
        pieDataSet.setColors(colorList);
        pieDataSet.setValueTextColor(-16777216);
        pieDataSet.setValueTextSize(16F);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(sleepDetailChart));
        sleepDetailChart.setData(pieData);
        sleepDetailChart.invalidate();
    }

    private void setChartDisplay() {
        sleepDetailChart.setUsePercentValues(true);
        Description description = new Description();
        description.setEnabled(false);
        sleepDetailChart.setDescription(description);
        sleepDetailChart.animate();
        sleepDetailChart.setDrawEntryLabels(false);
        Legend legend = sleepDetailChart.getLegend();
        setLegendChartDisplay(legend);
    }

    private void setLegendChartDisplay(Legend legend) {
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(12f);
        setChartData();
    }


    private String intToHour(int hourInt) {
        String text = "";
        if (hourInt / 60 >= 1) {
            text = hourInt / 60 + " h " + hourInt % 60 + " min";
        } else {
            text = hourInt % 60 + " min";
        }
        return text;
    }

    private String longToHour(Long hourLong) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(hourLong);
    }

    private int percentageCalculate(int totalSleep, int sleepToCalculate) {
        return sleepToCalculate * 100 / totalSleep;
    }

}
