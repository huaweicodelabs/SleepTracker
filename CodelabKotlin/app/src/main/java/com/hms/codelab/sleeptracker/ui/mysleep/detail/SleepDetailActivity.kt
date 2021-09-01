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

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.utils.Constants
import com.hms.codelab.sleeptracker.utils.SleepData
import kotlinx.android.synthetic.main.activity_sleep_detail.*
import java.text.SimpleDateFormat
import java.util.*

class SleepDetailActivity : AppCompatActivity() {
    private var sleepDataObj: SleepData? = null
    private lateinit var sleepDetailChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_detail)
        sleepDetailChart = findViewById(R.id.sleepDetailChart)
        sleepDataObj = intent.getParcelableExtra(Constants.sleepDataStr)
        setChartDisplay()
        setTextViews()
        totalSleepCardView.setOnClickListener {
            val dialog = ExplanationDialogFragment(Constants.DialogType.TOTAL.sleepType)
            dialog.show(supportFragmentManager, Constants.explanationFragmentTAG)
        }
        deepSleepCardView.setOnClickListener {
            val dialog = ExplanationDialogFragment(Constants.DialogType.DEEP.sleepType)
            dialog.show(supportFragmentManager, Constants.explanationFragmentTAG)
        }
        lightSleepCardView.setOnClickListener {
            val dialog = ExplanationDialogFragment(Constants.DialogType.LIGHT.sleepType)
            dialog.show(supportFragmentManager, Constants.explanationFragmentTAG)
        }
        remSleepCardView.setOnClickListener {
            val dialog = ExplanationDialogFragment(Constants.DialogType.REM.sleepType)
            dialog.show(supportFragmentManager, Constants.explanationFragmentTAG)
        }
        awakeCountCardView.setOnClickListener {
            val dialog = ExplanationDialogFragment(Constants.DialogType.AWAKE.sleepType)
            dialog.show(supportFragmentManager, Constants.explanationFragmentTAG)
        }
    }

    private fun setTextViews() {
        val sleepTimeText = "You slept ${intToHour(sleepDataObj!!.totalSleepTime)}"
        sleepTimeDetailTv.text = sleepTimeText
        sleepScoreValDetailTv.text = sleepDataObj!!.sleepScore.toString()
        val totalSleepTimeText = "${Constants.totalSleepTitle}: ${intToHour(sleepDataObj!!.totalSleepTime)}"
        totalSleepTv.text = totalSleepTimeText
        if (sleepDataObj!!.totalSleepTime < 360) {
            totalSleepTv.setTextColor(Constants.blue)
        } else if (sleepDataObj!!.totalSleepTime > 600) {
            totalSleepTv.setTextColor(Constants.red)
        }
        val deepSleepPercentage = percentageCalculate(
            sleepDataObj!!.totalSleepTime,
            sleepDataObj!!.deepSleepTime
        )
        val deepSleepTimeText = "${Constants.deepSleepTitle}: $deepSleepPercentage %"
        deepSleepDetailTv.text = deepSleepTimeText
        if (deepSleepPercentage < 20) {
            deepSleepDetailTv.setTextColor(Constants.blue)
        } else if (deepSleepPercentage > 60) {
            deepSleepDetailTv.setTextColor(Constants.red)
        }
        val lightSleepPercentage = percentageCalculate(
            sleepDataObj!!.totalSleepTime,
            sleepDataObj!!.lightSleepTime
        )
        val lightSleepTimeText = "${Constants.lightSleepTitle}: $lightSleepPercentage %"
        lightSleepDetailTv.text = lightSleepTimeText
        if (lightSleepPercentage >= 55) {
            lightSleepDetailTv.setTextColor(Constants.red)
        }
        val remSleepPercentage = percentageCalculate(
            sleepDataObj!!.totalSleepTime,
            sleepDataObj!!.remSleepTime
        )
        val remSleepTimeText = "${Constants.remSleepTitle}: $remSleepPercentage %"
        remSleepDetailTv.text = remSleepTimeText
        if (remSleepPercentage < 10) {
            remSleepDetailTv.setTextColor(Constants.blue)
        } else if (remSleepPercentage > 30) {
            remSleepDetailTv.setTextColor(Constants.red)
        }
        val awakeCountText = "${Constants.awakeCountTitle}: ${sleepDataObj!!.wakeUpCount} times"
        awakeDetailTv.text = awakeCountText
        if (sleepDataObj!!.wakeUpCount > 2) {
            awakeDetailTv.setTextColor(Constants.red)
        }
        bedTimeValTv.text = longToHour(sleepDataObj!!.fallAsleepTime)
        riseTimeValTv.text = longToHour(sleepDataObj!!.wakeUpTime)
    }

    private fun setChartData() {
        val dailySleepInfo = ArrayList<PieEntry>()
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj!!.lightSleepTime.toFloat(),
                Constants.lightSleepTitle
            )
        )
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj!!.deepSleepTime.toFloat(),
                Constants.deepSleepTitle
            )
        )
        dailySleepInfo.add(
            PieEntry(
                sleepDataObj!!.remSleepTime.toFloat(),
                Constants.remSleepTitle
            )
        )
        val pieDataSet = PieDataSet(dailySleepInfo, "")
        pieDataSet.colors = Constants.chartColorsPOY.toMutableList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 12F
        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter(sleepDetailChart))
        sleepDetailChart.data = pieData
        sleepDetailChart.invalidate()
    }

    private fun setChartDisplay() {
        sleepDetailChart.setUsePercentValues(true)
        sleepDetailChart.description.isEnabled = false
        sleepDetailChart.animate()
        sleepDetailChart.setDrawEntryLabels(false)
        val legend = sleepDetailChart.legend
        setLegendChartDisplay(legend)
    }

    private fun setLegendChartDisplay(legend: Legend) {
        legend.form = Legend.LegendForm.CIRCLE
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.textSize = 12f
        setChartData()
    }


    private fun intToHour(hourInt: Int): String {
        return if (hourInt / 60 >= 1) {
            "${hourInt / 60} h ${hourInt % 60} min"
        } else {
            "${hourInt % 60} min"
        }
    }

    private fun longToHour(hourLong: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(hourLong)
    }

    private fun percentageCalculate(totalSleep: Int, sleepToCalculate: Int): Int {
        return sleepToCalculate * 100 / totalSleep
    }


}