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
package com.hms.codelab.sleeptracker.ui.mysleep

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.data.Status
import com.hms.codelab.sleeptracker.push.manager.PushManager
import com.hms.codelab.sleeptracker.ui.mysleep.detail.SleepDetailActivity
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper
import com.huawei.hms.hihealth.data.SampleSet
import com.hms.codelab.sleeptracker.utils.Constants
import com.hms.codelab.sleeptracker.utils.SleepData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MySleepFragment : Fragment() {

    private val viewModel: MySleepViewModel by viewModel()
    private val sharedPreferences: SharedPreferenceHelper by inject()

    private val splitSeparator =
        "*******************************" + System.lineSeparator()

    private lateinit var sleepChart: PieChart
    private lateinit var lightSleepTv: TextView
    private lateinit var deepSleepTv: TextView
    private lateinit var remSleepTv: TextView
    private lateinit var sleepScoreTv: TextView
    private lateinit var sleepScoreValTv: TextView
    private lateinit var noSleepDataTv: TextView
    private lateinit var sleepDataObj: SleepData
    private lateinit var materialCalendarView: MaterialCalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)
        sleepChart = rootView.findViewById(R.id.sleepPieChart)
        lightSleepTv = rootView.findViewById(R.id.lightSleepTv)
        deepSleepTv = rootView.findViewById(R.id.deepSleepTv)
        remSleepTv = rootView.findViewById(R.id.remSleepTv)
        sleepScoreTv = rootView.findViewById(R.id.sleepScoreTv)
        sleepScoreValTv = rootView.findViewById(R.id.sleepScoreValTv)
        noSleepDataTv = rootView.findViewById(R.id.noSleepDataTv)
        materialCalendarView = rootView.findViewById(R.id.materialCalendarView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (TextUtils.isEmpty(sharedPreferences.getPushToken()) || sharedPreferences.getPushToken() == Constants.nanStr) {
            PushManager.getDeviceIdToken(requireContext())
        }
        sleepDataObj = SleepData(
            Constants.defaultDate, 0, 0,
            0, 0, 0,
            0, 0, 0
        )
        viewModel.readToday()
        listenPreferencesData()
        initView()
    }

    private fun listenPreferencesData() {

        viewModel.dailyDataResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it?.responseType) {
                Status.SUCCESSFUL -> {
                    logger("Success read daily summation from HMS core")
                    if (it.data!!.isEmpty) {
                        setNoDataDisplay()
                        sleepDataObj.sleepDate = Constants.defaultDate
                    } else {
                        it.data?.let { sampleSet -> cleanSampleSet(sampleSet) }
                        setDataDisplay()
                    }
                    logger(splitSeparator)
                }
                Status.ERROR -> {
                    viewModel.silentSignIn()
                }
            }
        })
    }

    private fun initView() {
        setNoDataDisplay()
        detailBtn.setOnClickListener {
            if (sleepDataObj.sleepDate != Constants.defaultDate) {
                val intent = Intent(requireContext(), SleepDetailActivity::class.java)
                intent.putExtra(Constants.sleepDataStr, sleepDataObj)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please make sure you have data for this day",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        materialCalendarView.selectedDate = CalendarDay.today()
        materialCalendarView.state().edit().setMaximumDate(CalendarDay.today()).commit()
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            val selectedMonth: String = if (date.month < 10) {
                String.format("%02d", date.month)
            } else {
                (date.month).toString()
            }
            val selectedDay: String = if (date.day < 10) {
                String.format("%02d", date.day)
            } else {
                date.day.toString()
            }
            val selectedDate = ("${date.year}" + selectedMonth + selectedDay).toInt()
            viewModel.readDailyData(selectedDate, selectedDate)
        }
    }

    private fun setChartData() {
        val dailySleepInfo = ArrayList<PieEntry>()
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj.lightSleepTime.toFloat(),
                "${Constants.lightSleepTitle}: ${sleepDataObj.lightSleepTime}"
            )
        )
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj.deepSleepTime.toFloat(),
                "${Constants.deepSleepTitle}: ${sleepDataObj.deepSleepTime}"
            )
        )
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj.remSleepTime.toFloat(),
                "${Constants.remSleepTitle}: ${sleepDataObj.remSleepTime}"
            )
        )
        val pieDataSet = PieDataSet(dailySleepInfo, "")
        pieDataSet.colors = Constants.chartColorsPOY.toMutableList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16F
        val pieData = PieData(pieDataSet)
        sleepPieChart.data = pieData
        sleepPieChart.invalidate()
    }

    private fun setChartDisplay() {
        sleepPieChart.setUsePercentValues(true)
        sleepPieChart.description.text =
            "Total Sleep: ${intToHour(sleepDataObj.totalSleepTime)}"
        sleepPieChart.description.textSize = 14f
        sleepPieChart.description.xOffset = 30f
        sleepPieChart.description.yOffset = 230f
        sleepPieChart.animate()
        sleepPieChart.setDrawEntryLabels(false)
        val legend = sleepPieChart.legend
        setLegendChartDisplay(legend)
    }

    private fun setLegendChartDisplay(legend: Legend) {
        legend.isEnabled = false
        setChartData()
    }

    private fun setSleepTexts() {
        sleepScoreValTv.text = "${sleepDataObj.sleepScore}"
        val lightSleepText = "\uD83D\uDFE3 Light: ${intToHour(sleepDataObj.lightSleepTime)}"
        lightSleepTv.text = lightSleepText
        val deepSleepText = "\uD83D\uDFE0  Deep: ${intToHour(sleepDataObj.deepSleepTime)}"
        deepSleepTv.text = deepSleepText
        val remSleepText = "\uD83D\uDFE1 Rem: ${intToHour(sleepDataObj.remSleepTime)}"
        remSleepTv.text = remSleepText
    }

    private fun cleanSampleSet(sampleSet: SampleSet) {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sampleSet.samplePoints.forEachIndexed { index, samplePoint ->
            val date = dateFormat.format(Date(samplePoint.getEndTime(TimeUnit.MILLISECONDS)))
            sleepDataObj.sleepDate = date
            samplePoint.dataType.fields.forEachIndexed { index, field ->
                when {
                    field.name.toString() == "light_sleep_time" -> {
                        sleepDataObj.lightSleepTime =
                            samplePoint.getFieldValue(field).asIntValue()
                    }
                    field.name.toString() == "deep_sleep_time" -> {
                        sleepDataObj.deepSleepTime =
                            samplePoint.getFieldValue(field).asIntValue()
                    }
                    field.name.toString() == "dream_time" -> {
                        sleepDataObj.remSleepTime =
                            samplePoint.getFieldValue(field).asIntValue()
                    }
                    field.name == "all_sleep_time" -> {
                        sleepDataObj.totalSleepTime =
                            samplePoint.getFieldValue(field).asIntValue()

                    }
                    field.name.toString() == "sleep_score" -> {
                        sleepDataObj.sleepScore = samplePoint.getFieldValue(field).asIntValue()
                    }
                    field.name == "wakeup_count" -> {
                        sleepDataObj.wakeUpCount = samplePoint.getFieldValue(field).asIntValue()
                    }
                    field.name == "fall_asleep_time" -> {
                        sleepDataObj.fallAsleepTime =
                            samplePoint.getFieldValue(field).asLongValue()
                    }
                    field.name == "wakeup_time" -> {
                        sleepDataObj.wakeUpTime = samplePoint.getFieldValue(field).asLongValue()
                    }
                }
            }
        }
        setChartDisplay()
        setSleepTexts()
    }

    private fun setNoDataDisplay() {
        sleepChart.visibility = View.INVISIBLE
        lightSleepTv.visibility = View.INVISIBLE
        deepSleepTv.visibility = View.INVISIBLE
        remSleepTv.visibility = View.INVISIBLE
        sleepScoreTv.visibility = View.INVISIBLE
        sleepScoreValTv.visibility = View.INVISIBLE
        noSleepDataTv.visibility = View.VISIBLE
    }

    private fun setDataDisplay() {
        sleepChart.visibility = View.VISIBLE
        lightSleepTv.visibility = View.VISIBLE
        deepSleepTv.visibility = View.VISIBLE
        remSleepTv.visibility = View.VISIBLE
        sleepScoreTv.visibility = View.VISIBLE
        sleepScoreValTv.visibility = View.VISIBLE
        noSleepDataTv.visibility = View.INVISIBLE
    }

    private fun intToHour(hourInt: Int): String {
        return if (hourInt / 60 >= 1) {
            "${hourInt / 60} h ${hourInt % 60} min"
        } else {
            "${hourInt % 60} min"
        }
    }

    private fun logger(string: String) {
        Log.i(Constants.mySleepFragmentTAG, string)
    }
}
