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

package com.hms.codelab.sleeptracker.ui.mysleep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.push.manager.PushManager;
import com.hms.codelab.sleeptracker.ui.mysleep.detail.SleepDetailActivity;
import com.hms.codelab.sleeptracker.ui.profile.ProfileViewModel;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper;
import com.hms.codelab.sleeptracker.utils.SleepData;
import com.huawei.hms.hihealth.data.SampleSet;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kotlin.Lazy;
import kotlin.jvm.internal.markers.KMutableList;

import static org.koin.java.KoinJavaComponent.inject;

public class MySleepFragment extends Fragment {
    private final Lazy<MySleepViewModel> viewModel = inject(MySleepViewModel.class);
    private final Lazy<SharedPreferenceHelper> sharedPreferences = inject(SharedPreferenceHelper.class);

    private String splitSeparator =
            "*******************************" + System.lineSeparator();

    private PieChart sleepChart;
    private TextView lightSleepTv;
    private TextView deepSleepTv;
    private TextView remSleepTv;
    private TextView sleepScoreTv;
    private TextView sleepScoreValTv;
    private TextView noSleepDataTv;
    private SleepData sleepDataObj;
    private MaterialCalendarView materialCalendarView;
    private Button detailBtn;
    private PieChart sleepPieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        sleepChart = rootView.findViewById(R.id.sleepPieChart);
        lightSleepTv = rootView.findViewById(R.id.lightSleepTv);
        deepSleepTv = rootView.findViewById(R.id.deepSleepTv);
        remSleepTv = rootView.findViewById(R.id.remSleepTv);
        sleepScoreTv = rootView.findViewById(R.id.sleepScoreTv);
        sleepScoreValTv = rootView.findViewById(R.id.sleepScoreValTv);
        noSleepDataTv = rootView.findViewById(R.id.noSleepDataTv);
        materialCalendarView = rootView.findViewById(R.id.materialCalendarView);
        detailBtn = rootView.findViewById(R.id.detailBtn);
        sleepPieChart = rootView.findViewById(R.id.sleepPieChart);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(sharedPreferences.getValue().getPushToken()) || sharedPreferences.getValue().getPushToken().equals(Constants.nanStr)) {
            PushManager.getDeviceIdToken(requireContext());
        }
        sleepDataObj = new SleepData(
                Constants.defaultDate, 0, 0,
                0, 0, 0,
                0, 0, 0
        );
        viewModel.getValue().readToday();
        listenPreferencesData();
        initView();
    }

    private void listenPreferencesData() {
        viewModel.getValue().dailyDataResultLiveData.observe(getViewLifecycleOwner(), sampleSetData -> {
            switch (sampleSetData.getResponseType()) {
                case ERROR:
                    // TODO Do silent sign in
                case SUCCESSFUL:
                    // TODO If data is empty set no data display otherwise clean the data and set data display
                case LOADING:
                    Log.i(Constants.mySleepFragmentTAG, "Loading");
            }
        });
    }

    private void initView() {
        setNoDataDisplay();
        detailBtn.setOnClickListener(view -> {
            if (!sleepDataObj.sleepDate.equals(Constants.defaultDate)) {
                Intent intent = new Intent(requireContext(), SleepDetailActivity.class);
                intent.putExtra(Constants.sleepDataStr, sleepDataObj);
                startActivity(intent);
            } else {
                Toast.makeText(
                        requireContext()
                        , "Please make sure you have data for this day",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.state().edit().setMaximumDate(CalendarDay.today()).commit();
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedMonth = "";
            if (date.getMonth() < 10) {
                selectedMonth = String.format("%02d", date.getMonth());
            } else {
                selectedMonth = String.valueOf((date.getMonth()));
            }
            String selectedDay = "";
            if (date.getDay() < 10) {
                selectedDay = String.format("%02d", date.getDay());
            } else {
                selectedDay = String.valueOf(date.getDay());
            }
            int selectedDate = Integer.parseInt(date.getYear() + selectedMonth + selectedDay);
            viewModel.getValue().readDailyData(selectedDate, selectedDate);
        });
    }

    private void setChartData() {
        ArrayList<PieEntry> dailySleepInfo = new ArrayList<PieEntry>();
        dailySleepInfo.add(
                new PieEntry(
                        (float) sleepDataObj.lightSleepTime,
                        Constants.lightSleepTitle + ": " + sleepDataObj.lightSleepTime
                )
        );
        dailySleepInfo.add(
                new PieEntry(
                        (float) sleepDataObj.deepSleepTime,
                        Constants.deepSleepTitle + ": " + sleepDataObj.deepSleepTime
                )
        );
        dailySleepInfo.add(
                new PieEntry(
                        (float) sleepDataObj.remSleepTime,
                        Constants.remSleepTitle + ": " + sleepDataObj.remSleepTime
                )
        );
        PieDataSet pieDataSet = new PieDataSet(dailySleepInfo, "");
        int[] colorList = new int[]{Color.rgb(148, 0, 211), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0)};
        pieDataSet.setColors(colorList);
        pieDataSet.setValueTextColor(-16777216);
        pieDataSet.setValueTextSize(16F);
        PieData pieData = new PieData(pieDataSet);
        sleepPieChart.setData(pieData);
        sleepPieChart.invalidate();
    }

    private void setChartDisplay() {
        sleepPieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Total Sleep: " + intToHour(sleepDataObj.totalSleepTime));
        description.setTextSize(14F);
        description.setXOffset(30F);
        description.setYOffset(230F);
        sleepPieChart.setDescription(description);
        sleepPieChart.animate();
        sleepPieChart.setDrawEntryLabels(false);
        Legend legend = sleepPieChart.getLegend();
        setLegendChartDisplay(legend);
    }

    private void setLegendChartDisplay(Legend legend) {
        legend.setEnabled(false);
        setChartData();
    }

    private void setSleepTexts() {
        sleepScoreValTv.setText(String.format("%d", sleepDataObj.sleepScore));
        String lightSleepText = "\uD83D\uDFE3 Light: " + intToHour(sleepDataObj.lightSleepTime);
        lightSleepTv.setText(lightSleepText);
        String deepSleepText = "\uD83D\uDFE0  Deep:  " + intToHour(sleepDataObj.deepSleepTime);
        deepSleepTv.setText(deepSleepText);
        String remSleepText = "\uD83D\uDFE1 Rem: " + intToHour(sleepDataObj.remSleepTime);
        remSleepTv.setText(remSleepText);
    }

    private void cleanSampleSet(SampleSet sampleSet) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sampleSet.getSamplePoints().forEach(samplePoint -> {
            sleepDataObj.sleepDate = dateFormat.format(new Date(samplePoint.getEndTime(TimeUnit.MILLISECONDS)));
            samplePoint.getDataType().getFields().forEach(field -> {
                switch (field.getName()) {
                    case "light_sleep_time":
                        sleepDataObj.lightSleepTime =
                                samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "deep_sleep_time":
                        sleepDataObj.deepSleepTime =
                                samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "dream_time":
                        sleepDataObj.remSleepTime =
                                samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "all_sleep_time":
                        sleepDataObj.totalSleepTime =
                                samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "sleep_score":
                        sleepDataObj.sleepScore = samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "wakeup_count":
                        sleepDataObj.wakeUpCount = samplePoint.getFieldValue(field).asIntValue();
                        break;
                    case "fall_asleep_time":
                        sleepDataObj.fallAsleepTime =
                                samplePoint.getFieldValue(field).asLongValue();
                        break;
                    case "wakeup_time":
                        sleepDataObj.wakeUpTime = samplePoint.getFieldValue(field).asLongValue();
                        break;
                }
            });
        });

        setChartDisplay();
        setSleepTexts();
    }

    private void setNoDataDisplay() {
        sleepChart.setVisibility(View.INVISIBLE);
        lightSleepTv.setVisibility(View.INVISIBLE);
        deepSleepTv.setVisibility(View.INVISIBLE);
        remSleepTv.setVisibility(View.INVISIBLE);
        sleepScoreTv.setVisibility(View.INVISIBLE);
        sleepScoreValTv.setVisibility(View.INVISIBLE);
        noSleepDataTv.setVisibility(View.VISIBLE);
    }

    private void setDataDisplay() {
        sleepChart.setVisibility(View.VISIBLE);
        lightSleepTv.setVisibility(View.VISIBLE);
        deepSleepTv.setVisibility(View.VISIBLE);
        remSleepTv.setVisibility(View.VISIBLE);
        sleepScoreTv.setVisibility(View.VISIBLE);
        sleepScoreValTv.setVisibility(View.VISIBLE);
        noSleepDataTv.setVisibility(View.INVISIBLE);
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

    private void logger(String string) {
        Log.i(Constants.mySleepFragmentTAG, string);
    }
}
