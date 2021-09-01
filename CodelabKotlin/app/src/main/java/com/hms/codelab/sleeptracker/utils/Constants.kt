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
package com.hms.codelab.sleeptracker.utils

import android.graphics.Color

object Constants {
    const val packageName = "com.huaweitr.sleeptracker"

    //TAG
    const val loginActivityTAG = "LoginActivity"
    const val pushServiceTAG = "PushService"
    const val mySleepFragmentTAG = "MySleepFragment"
    const val alarmScanActivityTAG = "AlarmScanActivity"
    const val silentSignInTAG = "SilentSignIn"
    const val explanationFragmentTAG = "explanationFragment"
    val chartColorsPOY = arrayOf(
        Color.rgb(148, 0, 211),
        Color.rgb(255, 102, 0),
        Color.rgb(245, 199, 0)
    )

    val blue = Color.rgb(135, 206, 235)
    val red = Color.rgb(220, 20, 60)


    const val REQUEST_SIGN_IN_LOGIN = 1002

    enum class DialogType(val sleepType: String) {
        LIGHT("lightSleep"),
        DEEP("deepSleep"),
        REM("remSleep"),
        AWAKE("awakeCount"),
        TOTAL("totalSleep")
    }

    const val sleepTypeStr = "sleepType"
    const val sleepTimeStr = "sleepTime"
    const val wakeUpTimeStr = "wakeUpTime"
    const val pushTokenStr = "pushToken"
    const val alarmSwitchStateStr = "alarmSwitchState"
    const val nanStr = "NaN"
    const val sleepStr = "sleep"
    const val wakeStr = "wake"
    const val sleepReportStr = "sleepReport"
    const val alarmClockStr = "alarmClock"
    const val defaultWakeUpTime = "07.00"
    const val defaultSleepTime = "23.00"

    const val grantType = "client_credentials"
    const val appSecret = "64e9f3e376848dcc1ecbe78e0e2cce2c4afdf9b495ea1fac91549e3fbb602675"
    const val appId = "104356813"

    const val defaultDate = "19700101"
    const val sleepDataStr = "sleepData"
    const val sleepDetailStr = "sleepDetail"

    const val CHANNEL_ID = "1010"

    const val alarmTitle = "Good Morning"
    const val alarmBody = "Rise and Shine"

    const val pushTitle = "Push Notification"
    const val pushBody = "Push Service"
    const val alarmServiceStr = "AlarmService"

    const val rescheduleTitle = "Rescheduling Alarms"
    const val rescheduleBody = "Alarm Clock Service"

    const val alarmSwitchState = "alarmSwitchState"

    const val alarmOffQRResult = "ALARM_OFF"

    const val goodNightTitle = "Good Night"
    val goodNightBody = arrayOf(
        "Sleeping time is coming.",
        "Get ready to go to bed.",
        "Always end the day with a positive thought. " +
                "No matter how hard things were, tomorrow's a fresh opportunity to make it better.",
        "Good night. The internet is closed for the night. Go to sleep, and get those great ideas percolating.",
        "The day is over, night has come. Today is gone, what’s done is done. " +
                "Embrace your dreams, through the night, tomorrow comes with a whole new light. – By George Orwell"
    )
    const val goodMorningTitle = "Good Morning"
    val goodMorningBody = arrayOf(
        "The journey of a thousand miles begins with one step.” – Lao Tzu",
        "You don’t have to be great to start, but you have to start to be great. — Zig Ziglar",
        "Opportunities are like sunrises. If you wait too long, you miss them. – William Arthur Ward",
        "A year from now, you may wish you had started today. — Karen Lamb",
        "Your future is created by what you do today, not tomorrow. — Robert Kiyosaki",
        "What you do today can improve all your tomorrows. — Ralph Marston"
    )
    const val sleepReportTitle = "Sleep Report"
    const val sleepReportBody = "Your sleep report is ready, don't forget to check it out."
    const val notificationErrorTitle = "Houston,"
    const val notificationErrorBody = "We have a problem."

    const val totalSleepTitle = "Total Sleep Time"
    const val totalSleepRef = "1. Importance of Sleep \n " +
            "Sleep is important to a number of brain functions, including how nerve cells (neurons) communicate with each other. " +
            "In fact, your brain and body stay remarkably active while you sleep. " +
            "Recent findings suggest that sleep plays a housekeeping role that removes toxins in your brain that build up while you are awake. " +
            "Without sleep you can’t form or maintain the pathways in your brain that let you learn and create new memories, " +
            "and it’s harder to concentrate and respond quickly. \n" +
            "2. How Much Sleep Do You Need?\n" +
            "Your need for sleep and your sleep patterns change as you age, but this varies significantly across individuals of the same age. " +
            "There is no magic “number of sleep hours” that works for everybody of the same age. " +
            "Babies initially sleep as much as 16 to 18 hours per day, which may boost growth and development (especially of the brain). " +
            "School-age children and teens on average need about 9.5 hours of sleep per night. " +
            "Most adults need 7-9 hours of sleep a night, but after age 60, nighttime sleep tends to be shorter, lighter, and interrupted by multiple awakenings. " +
            "Elderly people are also more likely to take medications that interfere with sleep. "

    const val deepSleepTitle = "Deep Sleep"
    const val deepSleepRef = "1. What is deep sleep? \n" +
            "Deep Sleep is the period of sleep that you need to feel refreshed in the morning. " +
            "It occurs in longer periods during the first half of the night. " +
            "Your heartbeat and breathing slow to their lowest levels during sleep. " +
            "Your muscles are relaxed and it may be difficult to awaken you. " +
            "Brain waves become even slower."

    const val lightSleepTitle = "Light Sleep"
    const val lightSleepRef = "1. What is light sleep? \n " +
            "Early light sleep is the changeover from wakefulness to sleep. " +
            "During this short period (lasting several minutes) of relatively light sleep, your heartbeat, breathing, and eye movements slow, " +
            "and your muscles relax with occasional twitches. " +
            "Your brain waves begin to slow from their daytime wakefulness patterns. \n " +
            "Light sleep is a period before you enter deeper sleep. " +
            "Your heartbeat and breathing slow, and muscles relax even further. " +
            "Your body temperature drops and eye movements stop. " +
            "Brain wave activity slows but is marked by brief bursts of electrical activity. " +
            "You spend more of your repeated sleep cycles in stage 2 sleep than in other sleep stages."

    const val remSleepTitle = "Rem Sleep"
    const val remSleepRef = "1. What is rapid eye movement (REM) sleep? \n" +
            "REM sleep first occurs about 90 minutes after falling asleep. " +
            "Your eyes move rapidly from side to side behind closed eyelids. " +
            "Mixed frequency brain wave activity becomes closer to that seen in wakefulness. " +
            "Your breathing becomes faster and irregular, and your heart rate and blood pressure increase to near waking levels. " +
            "Most of your dreaming occurs during REM sleep, although some can also occur in non-REM sleep. " +
            "Your arm and leg muscles become temporarily paralyzed, which prevents you from acting out your dreams. " +
            "As you age, you sleep less of your time in REM sleep. " +
            "Dreams can be experienced in all stages of sleep but usually are most vivid in REM sleep. " +
            "Some people dream in color, while others only recall dreams in black and white."


    const val awakeCountTitle = "Awake Count"
    const val awakeCountRef = "1. Nocturnal awakenings \n" +
            "Awaking during the sleep may cause for several reasons like insomnia, sleep deprivation, oversleeping or due to cold." +
            "Waking up during sleep can be accepted as normal if it is not too much. Too much definition depends on the person. " +
            "In general speaking waking up fewer than 2 times per night is okay. Older people face this issue more than young people. " +
            "To be careful about nocturnal awakening is important. Insomnia can make your energy levels to go low and can make " +
            "face other problems. If you are thinking your awake count is higher than it should be ask for professional medical help."

    const val refLink =
        "https://www.ninds.nih.gov/Disorders/Patient-Caregiver-Education/Understanding-Sleep"
}

object SerializedNames {
    const val android = "android"
    const val title = "title"
    const val notification = "notification"
    const val token = "token"
    const val body = "body"
    const val clickAction = "click_action"
    const val type = "type"
    const val intent = "intent"
}