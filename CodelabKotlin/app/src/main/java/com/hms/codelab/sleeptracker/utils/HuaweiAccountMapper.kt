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

import com.hms.codelab.sleeptracker.data.SignInResult
import com.huawei.hms.hihealth.data.Scopes
import com.huawei.hms.support.api.entity.auth.Scope
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.result.AuthHuaweiId

private typealias HuaweiScope = Scope

class HuaweiAccountMapper {
    fun map(from: AuthHuaweiId): SignInResult = SignInResult(
        token = from.accessToken,
        scopes = getGrantedScopes(from)
    )

    private fun getGrantedScopes(account: AuthHuaweiId): Set<Scope> {
        val scopes = HashSet<Scope>()
        if (account.authorizedScopes.contains(HuaweiScope("email"))) {
            scopes.add(HuaweiIdAuthAPIManager.HUAWEIID_BASE_SCOPE)
            scopes.add(Scope(Scopes.HEALTHKIT_SLEEP_READ))
            scopes.add(Scope(Scopes.HEALTHKIT_STEP_READ))
        }
        return scopes
    }
}