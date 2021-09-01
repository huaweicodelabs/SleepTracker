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

package com.hms.codelab.sleeptracker.utils;

import android.graphics.Color;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Constants {
    @NotNull
    public static final String packageName = "com.hms.codelab.sleeptracker";
    @NotNull
    public static final String loginActivityTAG = "LoginActivity";
    @NotNull
    public static final String pushServiceTAG = "PushService";
    @NotNull
    public static final String mySleepFragmentTAG = "MySleepFragment";
    @NotNull
    public static final String alarmScanActivityTAG = "AlarmScanActivity";
    @NotNull
    public static final String silentSignInTAG = "SilentSignIn";
    @NotNull
    public static final String explanationFragmentTAG = "explanationFragment";
    @NotNull
    private static final Integer[] chartColorsPOY;
//    private static final int blue;
//    private static final int red;
    public static int blue = Color.rgb(135, 206, 235);
    public static int red = Color.rgb(220, 20, 60);
    public static final int REQUEST_SIGN_IN_LOGIN = 1002;
    @NotNull
    public static final String sleepTypeStr = "sleepType";
    @NotNull
    public static final String sleepTimeStr = "sleepTime";
    @NotNull
    public static final String wakeUpTimeStr = "wakeUpTime";
    @NotNull
    public static final String pushTokenStr = "pushToken";
    @NotNull
    public static final String alarmSwitchStateStr = "alarmSwitchState";
    @NotNull
    public static final String nanStr = "NaN";
    @NotNull
    public static final String sleepStr = "sleep";
    @NotNull
    public static final String wakeStr = "wake";
    @NotNull
    public static final String sleepReportStr = "sleepReport";
    @NotNull
    public static final String alarmClockStr = "alarmClock";
    @NotNull
    public static final String defaultWakeUpTime = "07.00";
    @NotNull
    public static final String defaultSleepTime = "23.00";
    @NotNull
    public static final String grantType = "client_credentials";
    @NotNull
    public static final String appSecret = "64e9f3e376848dcc1ecbe78e0e2cce2c4afdf9b495ea1fac91549e3fbb602675";
    @NotNull
    public static final String appId = "104356813";
    @NotNull
    public static final String defaultDate = "19700101";
    @NotNull
    public static final String sleepDataStr = "sleepData";
    @NotNull
    public static final String sleepDetailStr = "sleepDetail";
    @NotNull
    public static final String CHANNEL_ID = "1010";
    @NotNull
    public static final String alarmTitle = "Good Morning";
    @NotNull
    public static final String alarmBody = "Rise and Shine";
    @NotNull
    public static final String pushTitle = "Push Notification";
    @NotNull
    public static final String pushBody = "Push Service";
    @NotNull
    public static final String alarmServiceStr = "AlarmService";
    @NotNull
    public static final String rescheduleTitle = "Rescheduling Alarms";
    @NotNull
    public static final String rescheduleBody = "Alarm Clock Service";
    @NotNull
    public static final String alarmSwitchState = "alarmSwitchState";
    @NotNull
    public static final String alarmOffQRResult = "ALARM_OFF";
    @NotNull
    public static final String goodNightTitle = "Good Night";
    @NotNull
    public static List<String> goodNightBody;
    @NotNull
    public static final String goodMorningTitle = "Good Morning";
    @NotNull
    public static List<String > goodMorningBody;
    @NotNull
    public static final String sleepReportTitle = "Sleep Report";
    @NotNull
    public static final String sleepReportBody = "Your sleep report is ready, don't forget to check it out.";
    @NotNull
    public static final String notificationErrorTitle = "Houston,";
    @NotNull
    public static final String notificationErrorBody = "We have a problem.";
    @NotNull
    public static final String totalSleepTitle = "Total Sleep Time";
    @NotNull
    public static final String totalSleepRef = "1. Importance of Sleep \n Sleep is important to a number of brain functions, including how nerve cells (neurons) communicate with each other. In fact, your brain and body stay remarkably active while you sleep. Recent findings suggest that sleep plays a housekeeping role that removes toxins in your brain that build up while you are awake. Without sleep you can’t form or maintain the pathways in your brain that let you learn and create new memories, and it’s harder to concentrate and respond quickly. \n2. How Much Sleep Do You Need?\nYour need for sleep and your sleep patterns change as you age, but this varies significantly across individuals of the same age. There is no magic “number of sleep hours” that works for everybody of the same age. Babies initially sleep as much as 16 to 18 hours per day, which may boost growth and development (especially of the brain). School-age children and teens on average need about 9.5 hours of sleep per night. Most adults need 7-9 hours of sleep a night, but after age 60, nighttime sleep tends to be shorter, lighter, and interrupted by multiple awakenings. Elderly people are also more likely to take medications that interfere with sleep. ";
    @NotNull
    public static final String deepSleepTitle = "Deep Sleep";
    @NotNull
    public static final String deepSleepRef = "1. What is deep sleep? \nDeep Sleep is the period of sleep that you need to feel refreshed in the morning. It occurs in longer periods during the first half of the night. Your heartbeat and breathing slow to their lowest levels during sleep. Your muscles are relaxed and it may be difficult to awaken you. Brain waves become even slower.";
    @NotNull
    public static final String lightSleepTitle = "Light Sleep";
    @NotNull
    public static final String lightSleepRef = "1. What is light sleep? \n Early light sleep is the changeover from wakefulness to sleep. During this short period (lasting several minutes) of relatively light sleep, your heartbeat, breathing, and eye movements slow, and your muscles relax with occasional twitches. Your brain waves begin to slow from their daytime wakefulness patterns. \n Light sleep is a period before you enter deeper sleep. Your heartbeat and breathing slow, and muscles relax even further. Your body temperature drops and eye movements stop. Brain wave activity slows but is marked by brief bursts of electrical activity. You spend more of your repeated sleep cycles in stage 2 sleep than in other sleep stages.";
    @NotNull
    public static final String remSleepTitle = "Rem Sleep";
    @NotNull
    public static final String remSleepRef = "1. What is rapid eye movement (REM) sleep? \nREM sleep first occurs about 90 minutes after falling asleep. Your eyes move rapidly from side to side behind closed eyelids. Mixed frequency brain wave activity becomes closer to that seen in wakefulness. Your breathing becomes faster and irregular, and your heart rate and blood pressure increase to near waking levels. Most of your dreaming occurs during REM sleep, although some can also occur in non-REM sleep. Your arm and leg muscles become temporarily paralyzed, which prevents you from acting out your dreams. As you age, you sleep less of your time in REM sleep. Dreams can be experienced in all stages of sleep but usually are most vivid in REM sleep. Some people dream in color, while others only recall dreams in black and white.";
    @NotNull
    public static final String awakeCountTitle = "Awake Count";
    @NotNull
    public static final String awakeCountRef = "1. Nocturnal awakenings \nAwaking during the sleep may cause for several reasons like insomnia, sleep deprivation, oversleeping or due to cold.Waking up during sleep can be accepted as normal if it is not too much. Too much definition depends on the person. In general speaking waking up fewer than 2 times per night is okay. Older people face this issue more than young people. To be careful about nocturnal awakening is important. Insomnia can make your energy levels to go low and can make face other problems. If you are thinking your awake count is higher than it should be ask for professional medical help.";
    @NotNull
    public static final String refLink = "https://www.ninds.nih.gov/Disorders/Patient-Caregiver-Education/Understanding-Sleep";
    @NotNull
    public static final Constants INSTANCE;

    @NotNull
    public final Integer[] getChartColorsPOY() {
        return chartColorsPOY;
    }

    public final int getBlue() {
        return blue;
    }

    public final int getRed() {
        return red;
    }

    @NotNull
    public final List<String > getGoodNightBody() {
        return goodNightBody;
    }

    @NotNull
    public final List<String> getGoodMorningBody() {
        return goodMorningBody;
    }

    private Constants() {
    }

    static {
        Constants var0 = new Constants();
        INSTANCE = var0;
        chartColorsPOY = new Integer[]{Color.rgb(148, 0, 211), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0)};
        blue = Color.rgb(135, 206, 235);
        red = Color.rgb(220, 20, 60);
        goodNightBody = Arrays.asList("Sleeping time is coming.", "Get ready to go to bed.", "Always end the day with a positive thought. No matter how hard things were, tomorrow's a fresh opportunity to make it better.", "Good night. The internet is closed for the night. Go to sleep, and get those great ideas percolating.", "The day is over, night has come. Today is gone, what’s done is done. Embrace your dreams, through the night, tomorrow comes with a whole new light. – By George Orwell");
        goodMorningBody = Arrays.asList("The journey of a thousand miles begins with one step.” – Lao Tzu", "You don’t have to be great to start, but you have to start to be great. — Zig Ziglar", "Opportunities are like sunrises. If you wait too long, you miss them. – William Arthur Ward", "A year from now, you may wish you had started today. — Karen Lamb", "Your future is created by what you do today, not tomorrow. — Robert Kiyosaki", "What you do today can improve all your tomorrows. — Ralph Marston");
    }

    public enum DialogType {
        AWAKE("awakeCount"),
        DEEP("deepSleep"),
        LIGHT("lightSleep"),
        REM("remSleep"),
        TOTAL("totalSleep");

        public final String sleepType;

        DialogType(String sleepType) {
            this.sleepType = sleepType;
        }
    }
}

