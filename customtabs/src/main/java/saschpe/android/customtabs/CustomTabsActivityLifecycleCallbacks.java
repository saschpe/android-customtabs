/*
 * Copyright 2017 Sascha Peilicke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package saschpe.android.customtabs;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public final class CustomTabsActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private CustomTabsHelper customTabsHelper;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        customTabsHelper = new CustomTabsHelper();
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        customTabsHelper.bindCustomTabsService(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        customTabsHelper.unbindCustomTabsService(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
