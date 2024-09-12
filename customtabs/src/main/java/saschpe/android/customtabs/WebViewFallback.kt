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

import android.content.Context
import android.content.Intent
import android.net.Uri
import saschpe.android.customtabs.CustomTabsHelper.CustomTabFallback

/**
 * Default [CustomTabsHelper.CustomTabFallback] implementation
 * that uses [WebViewActivity] to display the requested [Uri].
 */
class WebViewFallback : CustomTabFallback {
    /**
     * @param context The [Context] that wants to open the Uri
     * @param uri The [Uri] to be opened by the fallback
     */
    override fun openUri(context: Context?, uri: Uri?) {
        context?.startActivity(
            Intent(context, WebViewActivity::class.java).putExtra(
                WebViewActivity.EXTRA_URL,
                uri.toString(),
            ),
        )
    }
}
