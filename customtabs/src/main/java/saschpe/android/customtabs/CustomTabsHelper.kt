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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import saschpe.android.customtabs.CustomTabsPackageHelper.getPackageNameToUse

class CustomTabsHelper {
    private var customTabsSession: CustomTabsSession? = null
    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null
    private var connectionCallback: ConnectionCallback? = null

    /**
     * Unbinds the Activity from the Custom Tabs Service
     *
     * @param activity the activity that is connected to the service
     */
    fun unbindCustomTabsService(activity: Activity) {
        connection?.let {
            try {
                activity.unbindService(it)
            } catch (_: IllegalArgumentException) {
            }
            client = null
            customTabsSession = null
        }
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession
     *
     * @return a CustomTabsSession
     */
    val session: CustomTabsSession?
        get() {
            when {
                client == null -> customTabsSession = null
                customTabsSession == null -> customTabsSession = client?.newSession(null)
            }
            return customTabsSession
        }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
     */
    fun setConnectionCallback(callback: ConnectionCallback?) {
        connectionCallback = callback
    }

    /**
     * Binds the Activity to the Custom Tabs Service
     *
     * @param activity the activity to be bound to the service
     */
    fun bindCustomTabsService(activity: Activity?) {
        if (client != null) {
            return
        }
        val packageName = getPackageNameToUse(activity!!) ?: return
        val myConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                newClient: CustomTabsClient,
            ) {
                client = newClient
                client?.warmup(0L)
                connectionCallback?.onCustomTabsConnected()
                // Initialize a session as soon as possible.
                session
            }

            override fun onServiceDisconnected(name: ComponentName) {
                client = null
                connectionCallback?.onCustomTabsDisconnected()
            }

            override fun onBindingDied(name: ComponentName) {
                client = null
                connectionCallback?.onCustomTabsDisconnected()
            }
        }
        connection = myConnection
        CustomTabsClient.bindCustomTabsService(activity, packageName, myConnection)
    }

    fun mayLaunchUrl(uri: Uri, extras: Bundle?, otherLikelyBundles: List<Bundle?>?) =
        when (client) {
            null -> false
            else -> session?.mayLaunchUrl(uri, extras, otherLikelyBundles) ?: false
        }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected
     */
    interface ConnectionCallback {
        /**
         * Called when the service is connected
         */
        fun onCustomTabsConnected()

        /**
         * Called when the service is disconnected
         */
        fun onCustomTabsDisconnected()
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available
     */
    interface CustomTabFallback {
        /**
         * @param context The Activity that wants to open the Uri
         * @param uri The uri to be opened by the fallback
         */
        fun openUri(context: Context?, uri: Uri?)
    }

    companion object {
        private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "android.support.customtabs.extra.KEEP_ALIVE"

        /**
         * Opens the URL on a Custom Tab if possible. Otherwise, falls back to opening it on a WebView
         *
         * @param context The host activity
         * @param customTabsIntent customTabsIntent to be used if Custom Tabs is available
         * @param uri the Uri to be opened
         * @param fallback a CustomTabFallback to be used if Custom Tabs is not available
         */
        fun openCustomTab(
            context: Context,
            customTabsIntent: CustomTabsIntent,
            uri: Uri,
            fallback: CustomTabFallback?,
        ) {
            val packageName = getPackageNameToUse(context)
            // If we can't find a package name, it means there's no browser that supports Chrome Custom Tabs installed.
            // So, we fall back to the web-view
            if (packageName == null) {
                fallback?.openUri(context, uri)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    customTabsIntent.intent
                        .putExtra(
                            Intent.EXTRA_REFERRER,
                            Uri.parse("${Intent.URI_ANDROID_APP_SCHEME}//${context.packageName}"),
                        )
                }
                customTabsIntent.intent.setPackage(packageName)
                customTabsIntent.launchUrl(context, uri)
            }
        }

        fun addKeepAliveExtra(context: Context, intent: Intent) {
            intent.putExtra(
                EXTRA_CUSTOM_TABS_KEEP_ALIVE,
                Intent().apply {
                    setClassName(context.packageName, KeepAliveService::class.java.canonicalName as String)
                },
            )
        }
    }
}
