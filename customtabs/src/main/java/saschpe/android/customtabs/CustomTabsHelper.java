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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import java.util.List;

import saschpe.android.customtabs.utils.Utils;

/**
 * A helper wrapper help to opens the URL on a Custom Tab if possible. Otherwise fallsback to
 * opening it on a WebView
 */

public final class CustomTabsHelper {

    public static int UNDEFINED_RESOURCE = 0;

    private static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "android.support.customtabs.extra.KEEP_ALIVE";

    private CustomTabsSession customTabsSession;
    private CustomTabsClient client;
    private CustomTabsServiceConnection connection;
    private ConnectionCallback connectionCallback;

    public static class Builder {

        private final Context context;
        private final Uri uri;
        private int toolbarColor;
        private int toolbarDarkColor;
        private int closeButtonIcon;
        private int toolbarItemColor;
        private boolean openWebViewFallback = true;
        private boolean addKeepAliveExtra;

        private CustomTabsIntent.Builder customTabsIntentBuilder;

        /**
         * @param context The host activity
         * @param uri     the Uri to be opened
         */
        public Builder(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        public Builder setToolbarColor(@ColorInt int toolbarColor) {
            this.toolbarColor = toolbarColor;
            return this;
        }

        public Builder setToolbarDarkColor(int toolbarDarkColor) {
            this.toolbarDarkColor = toolbarDarkColor;
            return this;
        }

        public Builder setCloseButtonIcon(@DrawableRes int closeButtonIcon) {
            this.closeButtonIcon = closeButtonIcon;
            return this;
        }

        public Builder setToolbarItemColor(@ColorInt int toolbarItemColor) {
            this.toolbarItemColor = toolbarItemColor;
            return this;
        }

        public Builder setOpenWebViewFallback(boolean openWebViewFallback) {
            this.openWebViewFallback = openWebViewFallback;
            return this;
        }

        public Builder setAddKeepAliveExtra(boolean addKeepAliveExtra) {
            this.addKeepAliveExtra = addKeepAliveExtra;
            return this;
        }

        /**
         * Apply some sane defaults across a single app.
         * <b>
         * Not strictly necessary but simplifies code when having many different
         * custom tab intents in one app.
         *
         * @return {@link CustomTabsIntent.Builder}
         */
        public CustomTabsIntent.Builder getCustomTabsIntentBuilder() {
            if (customTabsIntentBuilder == null) {
                customTabsIntentBuilder = new CustomTabsIntent.Builder();
            }

            customTabsIntentBuilder
                    .addDefaultShareMenuItem()
                    .setToolbarColor(toolbarColor)
                    .setShowTitle(true);

            Bitmap backArrow = Utils.getBitmapFromVectorDrawable(context, closeButtonIcon, toolbarItemColor);
            if (backArrow != null) {
                customTabsIntentBuilder.setCloseButtonIcon(backArrow);
            }

            return customTabsIntentBuilder;
        }

        /**
         * @return true if open with Chrome or Webview, false if not open
         */
        public boolean open() {
            String packageName = CustomTabsPackageHelper.getPackageNameToUse(context);

            //If we cant find a package name, it means there's no browser that supports
            //Chrome Custom Tabs installed. So, we fallback to the web-view
            if (packageName == null) {
                if (openWebViewFallback) {
                    // build WebView activity fallback
                    WebViewFallback webViewFallback = new WebViewFallback()
                            .setToolbarColor(toolbarColor)
                            .setToolbarDarkColor(toolbarDarkColor)
                            .setCloseButtonIcon(closeButtonIcon)
                            .setToolbarItemColor(toolbarItemColor);

                    webViewFallback.openUri(context, uri);
                    return true;
                }
            } else {
                // build CustomTabsIntent
                CustomTabsIntent customTabsIntent = getCustomTabsIntentBuilder().build();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    customTabsIntent.intent
                            .putExtra(Intent.EXTRA_REFERRER,
                                    Uri.parse(Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName()));
                }

                if (addKeepAliveExtra) {
                    Intent keepAliveIntent = new Intent().setClassName(
                            context.getPackageName(),
                            KeepAliveService.class.getCanonicalName());
                    customTabsIntent.intent.putExtra(EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent);
                }

                customTabsIntent.intent.setPackage(packageName);
                customTabsIntent.launchUrl(context, uri);
                return true;
            }

            return false;
        }
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
