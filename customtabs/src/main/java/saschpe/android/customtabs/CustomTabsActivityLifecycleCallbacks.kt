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
package saschpe.android.customtabs

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class CustomTabsActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    private lateinit var customTabsHelper: CustomTabsHelper

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        customTabsHelper = CustomTabsHelper()
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) {
        customTabsHelper.bindCustomTabsService(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        customTabsHelper.unbindCustomTabsService(activity)
    }

    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
