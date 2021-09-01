
package com.hms.codelab.sleeptracker.di

import com.hms.codelab.sleeptracker.utils.DataControllerImpl
import com.hms.codelab.sleeptracker.utils.SettingsControllerImpl
import com.hms.codelab.sleeptracker.utils.SharedPreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mLocalModules = module {

    single {
        SharedPreferenceHelper(androidContext())
    }

    single {
        SettingsControllerImpl(androidContext()).getSettingsController()
    }

    single {
        DataControllerImpl(androidContext()).dataController
    }

}
