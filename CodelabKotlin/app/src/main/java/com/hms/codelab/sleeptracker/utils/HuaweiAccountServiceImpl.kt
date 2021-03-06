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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.hms.codelab.sleeptracker.R
import com.hms.codelab.sleeptracker.data.SignInResult
import com.hms.codelab.sleeptracker.data.Worker
import com.hms.codelab.sleeptracker.services.AccountService
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hmf.tasks.Task
import com.huawei.hms.hihealth.DataController
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.hihealth.data.DataType
import com.huawei.hms.hihealth.data.SampleSet
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import io.reactivex.Single
import java.lang.Boolean
import java.util.*

class HuaweiAccountServiceImpl(
    val context: Context,
    val dataController: DataController,
    private val mSettingController: SettingController
) : AccountService {
    private var mHuaweiIdAuthService: HuaweiIdAuthService
    private val mapper = HuaweiAccountMapper()

    init {
        val params = HuaweiIdAuthParamsHelper(
            HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM
        )
            .setAccessToken()
            .setScopeList(getScopes())
            .setIdToken()
            .setEmail()
            .createParams()

        mHuaweiIdAuthService = HuaweiIdAuthManager.getService(context, params)
    }


    private fun getScopes(): MutableList<Scope> {
        val scopeList: MutableList<Scope> = LinkedList()
        scopeList.add(HuaweiIdAuthAPIManager.HUAWEIID_BASE_SCOPE) // Basic account permissions.
        scopeList.add(Scope(Scopes.HEALTHKIT_SLEEP_READ))
        scopeList.add(Scope(Scopes.HEALTHKIT_STEP_READ))
        return scopeList
    }


    override fun silentSignIn(): Single<SignInResult> {
        return Single.create { emitter ->
            val task: Task<AuthHuaweiId> = mHuaweiIdAuthService.silentSignIn()
            task.addOnSuccessListener {
                emitter.onSuccess(mapper.map(it))
                user = it
            }
            task.addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun getSignInIntent(intent: (Intent) -> Unit) {
        intent.invoke(mHuaweiIdAuthService.signInIntent)
    }

    override fun onSignInActivityResult(intent: Intent): Single<SignInResult> {
        return Single.create { emitter ->
            val task: Task<AuthHuaweiId> = HuaweiIdAuthManager.parseAuthResultFromIntent(intent)
            task.addOnSuccessListener {
                Log.i(TAG, "onSignInActivityResult: ${it.email}")
                emitter.onSuccess(mapper.map(it))
                user = it
            }
            task.addOnFailureListener { emitter.onError(it) }
        }
    }

    override fun signOut(): Worker<Unit> {
        val worker: Worker<Unit> =
            Worker()

        mHuaweiIdAuthService.signOut()
            .addOnSuccessListener { worker.onSuccess(Unit) }
            .addOnFailureListener { worker.onFailure(it) }
            .addOnCanceledListener { worker.onCanceled() }

        return worker
    }

    override fun transmitTokenIntoAppGalleryConnect(accessToken: String): Single<SignInResult> {
        return Single.create { emitter ->
            val credential = HwIdAuthProvider.credentialWithToken(accessToken)
            AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener {
                    emitter.onSuccess(SignInResult(it.user.uid, setOf()))
                }
                .addOnFailureListener {
                    Log.e(Constants.loginActivityTAG, "Transmission Error: ${it.message}")
                }
        }
    }

    companion object {
        private const val TAG = "HuaweiAccountServiceImp"
        var user: AuthHuaweiId? = null
    }

    override fun readToday(): Single<SampleSet> {

        return Single.create { emitter ->
            val todaySummaryTask = dataController.readTodaySummation(DataType.DT_CONTINUOUS_SLEEP)
            todaySummaryTask.addOnSuccessListener { sampleSet ->
                emitter.onSuccess(sampleSet)
            }
            todaySummaryTask.addOnFailureListener { exception ->
                if (exception.message!!.contains("50005")) {
                    emitter.onError(exception)
                }
            }
        }
    }

    override fun readDailyData(startTime: Int, endTime: Int): Single<SampleSet> {

        return Single.create { emitter ->
            val dailySummationTask =
                dataController.readDailySummation(
                    DataType.DT_CONTINUOUS_SLEEP,
                    startTime,
                    endTime
                )
            dailySummationTask.addOnSuccessListener { sampleSet ->
                emitter.onSuccess(sampleSet)
            }
            dailySummationTask.addOnFailureListener { exception ->
                if (exception.message!!.contains("50005")) {
                    emitter.onError(exception)
                }
            }
        }
    }

    override fun checkOrAuthorizeHealth(intent: (Intent) -> Unit) {

        val healthAppSettingDataShareHealthKitActivityScheme =
            "huaweischeme://healthapp/achievement?module=kit"
        // Check the Health authorization status. If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
        val authTask = mSettingController.healthAppAuthorization
        authTask.addOnSuccessListener { result ->
            if (Boolean.TRUE == result) {
                Log.i(Constants.loginActivityTAG, context.getString(R.string.check_authorize_success))
            } else {
                // If the authorization has not been granted, display the authorization screen in the HUAWEI Health app.
                val healthKitSchemaUri: Uri =
                    Uri.parse(healthAppSettingDataShareHealthKitActivityScheme)
                val healtIntent = Intent(Intent.ACTION_VIEW, healthKitSchemaUri)
                // Check whether the authorization screen of the Health app can be displayed.
                if (healtIntent.resolveActivity(context.packageManager) != null) {
                    intent.invoke(healtIntent)
                } else {
                    Log.w(
                        Constants.loginActivityTAG,
                        context.getString(R.string.auth_not_resolve)
                    )
                }
            }
        }.addOnFailureListener { exception ->
            if (exception != null) {
                Log.i(Constants.loginActivityTAG, context.getString(R.string.check_authorize_exception))
                Log.i(Constants.loginActivityTAG, exception.message.toString())
            }
        }
    }


    override fun queryHealthAuthorization() {
        val queryTask = mSettingController.healthAppAuthorization
        queryTask.addOnSuccessListener { result ->
            if (Boolean.TRUE == result) {
                Log.i(
                    Constants.loginActivityTAG,
                    context.getString(R.string.query_authorized)
                )
            } else {
                Log.i(
                    Constants.loginActivityTAG,
                    context.getString(R.string.query_not_authorized)
                )
            }
        }.addOnFailureListener { exception ->
            if (exception != null) {
                Log.i(Constants.loginActivityTAG, context.getString(R.string.query_exception))
                Log.i(Constants.loginActivityTAG, exception.message.toString())
            }
        }
    }
}

