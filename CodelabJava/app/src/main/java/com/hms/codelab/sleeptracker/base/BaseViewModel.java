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

package com.hms.codelab.sleeptracker.base;

import androidx.lifecycle.ViewModel;


import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(@NotNull Disposable disposable){
        compositeDisposable.add(disposable);
    }

    protected void clearDisposable(){
        compositeDisposable.clear();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        clearDisposable();
    }
}