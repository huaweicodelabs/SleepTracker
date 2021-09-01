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

package com.hms.codelab.sleeptracker.ui.profile;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.hms.codelab.sleeptracker.R;
import com.hms.codelab.sleeptracker.alarm.service.AlarmService;
import com.hms.codelab.sleeptracker.data.Status;
import com.hms.codelab.sleeptracker.ui.login.LoginActivity;
import com.hms.codelab.sleeptracker.utils.Constants;
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.squareup.picasso.Picasso;

import kotlin.Lazy;

import static org.koin.java.KoinJavaComponent.inject;

public class ProfileFragment extends Fragment {
    private final Lazy<ProfileViewModel> viewModel = inject(ProfileViewModel.class);
    private final Lazy<SharedPreferenceHelper> sharedPreferences = inject(SharedPreferenceHelper.class);
    private final Lazy<AlarmService> alarmService = inject(AlarmService.class);

    private Integer mHour = 0;
    private Integer mMinute = 0;
    private TextView sleepTimeTV;
    private TextView wakeUpTimeTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        TextView profileNameTV = view.findViewById(R.id.profileNameTv);
        ImageView profileImageView = view.findViewById(R.id.profileImageView);
        profileNameTV.setText(user.getDisplayName());
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            Picasso.get().load(user.getPhotoUrl()).into(profileImageView);
        }
        sleepTimeTV = view.findViewById(R.id.sleepTimeValTv);
        wakeUpTimeTV = view.findViewById(R.id.wakeUpTimeValTv);

        viewModel.getValue().getPreferencesData();
        checkSwitchState();
        saveSwitchState();
        selectSleepTime();
        selectWakeUpTime();
        listenPreferencesData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Integer id = item.getItemId();
        if (id == R.id.buttonLogout) {
            viewModel.getValue().logout();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void listenPreferencesData() {
        viewModel.getValue().sleepTimeResultLiveData.observe(getViewLifecycleOwner(), it -> {
            if (it.getResponseType() == Status.SUCCESSFUL) {
                sleepTimeTV.setText(it.getData());
            }
        });

        viewModel.getValue().wakeUpTimeResultLiveData.observe(getViewLifecycleOwner(), it -> {
            if (it.getResponseType() == Status.SUCCESSFUL) {
                wakeUpTimeTV.setText(it.getData());
            }
        });
    }

    private void selectSleepTime() {
        sleepTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime(mHour, mMinute, Constants.sleepStr);
            }
        });
    }

    private void selectWakeUpTime() {
        wakeUpTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime(mHour, mMinute, Constants.wakeStr);
            }
        });
    }

    private void checkSwitchState() {
        SwitchCompat alarmClockSwitch = getView().findViewById(R.id.alarmClockSwitch);
        alarmClockSwitch.setChecked(sharedPreferences.getValue().getAlarmSwitchState());
    }

    private void saveSwitchState() {
        SwitchCompat alarmClockSwitch = getView().findViewById(R.id.alarmClockSwitch);
        alarmClockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                sharedPreferences.getValue().updateAlarmSwitchState(state);
                String wakeUpTime = sharedPreferences.getValue().getWakeUpTime();
                String hour = wakeUpTime.substring(0, 2);
                String minute = wakeUpTime.substring(wakeUpTime.length() - 2);
                if (state) {
                    alarmService.getValue().setRepetitiveAlarm(
                            Integer.parseInt(hour),
                            Integer.parseInt(minute),
                            Constants.alarmClockStr,
                            3
                    );
                } else {
                    alarmService.getValue().cancelAlarm(3);
                }
            }
        });
    }

    private void selectTime(Integer mHour, Integer mMinute, String sleepType) {
        switch (sleepType) {
            case Constants.sleepStr:
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                String sleepTime = String.format("%02d:%02d", hour, minute);
                                sleepTimeTV.setText(sleepTime);
                                sharedPreferences.getValue().updateSleepTime(sleepTime);
                                if (minute < 30) {
                                    int newHour = hour - 1;
                                    int newMin = minute + 30;
                                    alarmService.getValue().setRepetitiveAlarm(newHour, newMin, Constants.sleepStr, 0);
                                } else {
                                    alarmService.getValue().setRepetitiveAlarm(
                                            hour,
                                            minute - 30,
                                            Constants.sleepStr,
                                            0
                                    );
                                }
                            }
                        },
                        mHour,
                        mMinute,
                        true
                );
                timePickerDialog.show();
                break;
            case Constants.wakeStr:
                SwitchCompat alarmClockSwitch = getView().findViewById(R.id.alarmClockSwitch);
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                String wakeUpTime = String.format("%02d:%02d", hour, minute);
                                wakeUpTimeTV.setText(wakeUpTime);
                                sharedPreferences.getValue().updateWakeUpTime(wakeUpTime);
                                if (minute >= 30) {
                                    int newHour = hour + 1;
                                    int newMin = minute + 30;
                                    alarmService.getValue().setRepetitiveAlarm(newHour, newMin, Constants.wakeStr, 1);
                                    alarmService.getValue().setRepetitiveAlarm(
                                            newHour + 2,
                                            newMin,
                                            Constants.sleepReportStr,
                                            2
                                    );
                                } else {
                                    alarmService.getValue().setRepetitiveAlarm(hour, minute + 30, Constants.wakeStr, 1);
                                    alarmService.getValue().setRepetitiveAlarm(
                                            hour + 2,
                                            minute,
                                            Constants.sleepReportStr,
                                            2
                                    );
                                }
                                if (alarmClockSwitch.isChecked()) {
                                    sharedPreferences.getValue().updateAlarmSwitchState(true);
                                    alarmService.getValue().setRepetitiveAlarm(
                                            hour,
                                            minute,
                                            Constants.alarmClockStr,
                                            3
                                    );
                                }
                            }
                        },
                        mHour,
                        mMinute,
                        true
                );
                timePickerDialog1.show();
                break;
        }
    }
}
