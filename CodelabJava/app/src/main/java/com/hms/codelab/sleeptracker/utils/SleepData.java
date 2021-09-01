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

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class SleepData implements Parcelable {
    @NotNull
    public String sleepDate;
    public int totalSleepTime;
    public long fallAsleepTime;
    public long wakeUpTime;
    public int lightSleepTime;
    public int deepSleepTime;
    public int remSleepTime;
    public int wakeUpCount;
    public int sleepScore;

    public SleepData(@NotNull String sleepDate, int totalSleepTime, long fallAsleepTime, long wakeUpTime, int lightSleepTime, int deepSleepTime, int remSleepTime, int wakeUpCount, int sleepScore) {
        this.sleepDate = sleepDate;
        this.totalSleepTime = totalSleepTime;
        this.fallAsleepTime = fallAsleepTime;
        this.wakeUpTime = wakeUpTime;
        this.lightSleepTime = lightSleepTime;
        this.deepSleepTime = deepSleepTime;
        this.remSleepTime = remSleepTime;
        this.wakeUpCount = wakeUpCount;
        this.sleepScore = sleepScore;
    }

    protected SleepData(Parcel in) {
        sleepDate = in.readString();
        totalSleepTime = in.readInt();
        fallAsleepTime = in.readLong();
        wakeUpTime = in.readLong();
        lightSleepTime = in.readInt();
        deepSleepTime = in.readInt();
        remSleepTime = in.readInt();
        wakeUpCount = in.readInt();
        sleepScore = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sleepDate);
        dest.writeInt(totalSleepTime);
        dest.writeLong(fallAsleepTime);
        dest.writeLong(wakeUpTime);
        dest.writeInt(lightSleepTime);
        dest.writeInt(deepSleepTime);
        dest.writeInt(remSleepTime);
        dest.writeInt(wakeUpCount);
        dest.writeInt(sleepScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SleepData> CREATOR = new Creator<SleepData>() {
        @Override
        public SleepData createFromParcel(Parcel in) {
            return new SleepData(in);
        }

        @Override
        public SleepData[] newArray(int size) {
            return new SleepData[size];
        }
    };
}
