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
package com.hms.codelab.sleeptracker.ui.profile

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.alarm.service.AlarmService
import com.hms.codelab.sleeptracker.data.Status
import com.hms.codelab.sleeptracker.ui.login.LoginActivity
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper
import com.huawei.agconnect.auth.AGConnectAuth
import com.hms.codelab.sleeptracker.utils.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()
    private val sharedPreferences: SharedPreferenceHelper by inject()
    private val alarmService: AlarmService by inject()

    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var sleepTimeTV: TextView
    private lateinit var wakeUpTimeTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = AGConnectAuth.getInstance().currentUser
        profileNameTv.text = user.displayName
        if (user.photoUrl != null && user.photoUrl != "") {
            Picasso.get().load(user.photoUrl).into(profileImageView)
        }

        sleepTimeTV = view.findViewById(R.id.sleepTimeValTv)
        wakeUpTimeTV = view.findViewById(R.id.wakeUpTimeValTv)

        viewModel.getPreferencesData()
        checkSwitchState()
        saveSwitchState()
        selectSleepTime()
        selectWakeUpTime()
        listenPreferencesData()
    }

    private fun listenPreferencesData() {

        viewModel.sleepTimeResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it?.responseType) {
                Status.SUCCESSFUL -> {
                    sleepTimeTV.text = it.data
                }
            }
        })

        viewModel.wakeUpTimeResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it?.responseType) {
                Status.SUCCESSFUL -> {
                    wakeUpTimeTV.text = it.data
                }
            }
        })
    }

    private fun selectSleepTime() {
        sleepTimeTV.setOnClickListener {
            selectTime(mHour, mMinute, Constants.sleepStr)
        }
    }

    private fun selectWakeUpTime() {
        wakeUpTimeTV.setOnClickListener {
            selectTime(mHour, mMinute, Constants.wakeStr)
        }
    }

    private fun checkSwitchState() {
        alarmClockSwitch.isChecked = sharedPreferences.getAlarmSwitchState()
    }

    private fun saveSwitchState() {
        alarmClockSwitch.setOnCheckedChangeListener { compoundButton, state ->
            sharedPreferences.updateAlarmSwitchState(state)
            val wakeUpTime = sharedPreferences.getWakeUpTime()
            val hour = wakeUpTime!!.take(2)
            val minute = wakeUpTime.takeLast(2)
            if (state) {
                alarmService.setRepetitiveAlarm(
                    hour.toInt(),
                    minute.toInt(),
                    Constants.alarmClockStr,
                    3
                )
            } else {
                alarmService.cancelAlarm(3)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.buttonLogout) {
            viewModel.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity!!.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectTime(mHour: Int, mMinute: Int, sleepType: String) {
        when (sleepType) {
            Constants.sleepStr -> {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        val sleepTime = String.format("%02d:%02d", hour, minute)
                        sleepTimeTV.text = sleepTime
                        sharedPreferences.updateSleepTime(sleepTime)
                        if (minute < 30) {
                            val newHour = hour - 1
                            val newMin = minute + 30
                            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.sleepStr, 0)
                        } else {
                            alarmService.setRepetitiveAlarm(
                                hour,
                                minute - 30,
                                Constants.sleepStr,
                                0
                            )
                        }
                    },
                    mHour,
                    mMinute,
                    true
                )
                timePickerDialog.show()
            }
            Constants.wakeStr -> {
                val timePickerDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        val wakeUpTime = String.format("%02d:%02d", hour, minute)
                        wakeUpTimeTV.text = wakeUpTime
                        sharedPreferences.updateWakeUpTime(wakeUpTime)
                        if (minute >= 30) {
                            val newHour = hour + 1
                            val newMin = minute - 30
                            alarmService.setRepetitiveAlarm(newHour, newMin, Constants.wakeStr, 1)
                            alarmService.setRepetitiveAlarm(
                                newHour + 2,
                                newMin,
                                Constants.sleepReportStr,
                                2
                            )
                            if (alarmClockSwitch.isChecked) {
                                sharedPreferences.updateAlarmSwitchState(true)
                                alarmService.setRepetitiveAlarm(
                                    hour,
                                    minute,
                                    Constants.alarmClockStr,
                                    3
                                )
                            }
                        } else {
                            alarmService.setRepetitiveAlarm(hour, minute + 30, Constants.wakeStr, 1)
                            alarmService.setRepetitiveAlarm(
                                hour + 2,
                                minute,
                                Constants.sleepReportStr,
                                2
                            )
                            if (alarmClockSwitch.isChecked) {
                                sharedPreferences.updateAlarmSwitchState(true)
                                alarmService.setRepetitiveAlarm(
                                    hour,
                                    minute,
                                    Constants.alarmClockStr,
                                    3
                                )
                            }
                        }
                    },
                    mHour,
                    mMinute,
                    true
                )
                timePickerDialog.show()
            }
        }
    }
}