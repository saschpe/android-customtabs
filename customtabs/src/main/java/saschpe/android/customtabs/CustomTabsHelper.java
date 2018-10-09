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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import java.util.List;

public final class CustomTabsHelper {
    private static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "android.support.customtabs.extra.KEEP_ALIVE";

    private CustomTabsSession customTabsSession;
    private CustomTabsClient client;
    private CustomTabsServiceConnection connection;
    private ConnectionCallback connectionCallback;

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView
     *
     * @param context          The host activity
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
     * @param uri              the Uri to be opened
     * @param fallback         a CustomTabFallback to be used if Custom Tabs is not available
     */
    public static void openCustomTab(final Context context,
                                     final CustomTabsIntent customTabsIntent,
                                     final Uri uri,
                                     final CustomTabFallback fallback) {
        String packageName = CustomTabsPackageHelper.getPackageNameToUse(context);

        //If we cant find a package name, it means there's no browser that supports
        //Chrome Custom Tabs installed. So, we fallback to the web-view
        if (packageName == null) {
            if (fallback != null) {
                fallback.openUri(context, uri);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                customTabsIntent.intent
                        .putExtra(Intent.EXTRA_REFERRER,
                                Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName()));
            }

            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(context, uri);
        }
    }

    public static void addKeepAliveExtra(final Context context, final Intent intent) {
        Intent keepAliveIntent = new Intent().setClassName(
                context.getPackageName(),
                KeepAliveService.class.getCanonicalName());
        intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent);
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service
     *
     * @param activity the activity that is connected to the service
     */
    public void unbindCustomTabsService(final Activity activity) {
        if (connection == null) {
            return;
        }
        activity.unbindService(connection);
        client = null;
        customTabsSession = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession
     *
     * @return a CustomTabsSession
     */
    public CustomTabsSession getSession() {
        if (client == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            customTabsSession = client.newSession(null);
        }
        return customTabsSession;
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
     */
    public void setConnectionCallback(final ConnectionCallback connectionCallback) {
        this.connectionCallback = connectionCallback;
    }

    /**
     * Binds the Activity to the Custom Tabs Service
     *
     * @param activity the activity to be bound to the service
     */
    public void bindCustomTabsService(final Activity activity) {
        if (client != null) {
            return;
        }

        String packageName = CustomTabsPackageHelper.getPackageNameToUse(activity);
        if (packageName == null) {
            return;
        }
        connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                CustomTabsHelper.this.client = client;
                CustomTabsHelper.this.client.warmup(0L);
                if (connectionCallback != null) {
                    connectionCallback.onCustomTabsConnected();
                }
                //Initialize a session as soon as possible.
                getSession();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                client = null;
                if (connectionCallback != null) {
                    connectionCallback.onCustomTabsDisconnected();
                }
            }

            @Override
            public void onBindingDied(ComponentName name) {
                client = null;
                if (connectionCallback != null) {
                    connectionCallback.onCustomTabsDisconnected();
                }
            }
        };
        CustomTabsClient.bindCustomTabsService(activity, packageName, connection);
    }

    public boolean mayLaunchUrl(final Uri uri, final Bundle extras, final List<Bundle> otherLikelyBundles) {
        if (client == null) {
            return false;
        }
        CustomTabsSession session = getSession();
        return session != null && session.mayLaunchUrl(uri, extras, otherLikelyBundles);
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected
     */
    public interface ConnectionCallback {
        /**
         * Called when the service is connected
         */
        void onCustomTabsConnected();

        /**
         * Called when the service is disconnected
         */
        void onCustomTabsDisconnected();
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available
     */
    public interface CustomTabFallback {
        /**
         * @param context The Activity that wants to open the Uri
         * @param uri     The uri to be opened by the fallback
         */
        void openUri(Context context, Uri uri);
    }
}