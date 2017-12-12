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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

/**
 * Default {@link CustomTabsHelper.CustomTabFallback} implementation
 * that uses {@link WebViewActivity} to display the requested {@link Uri}.
 */
public final class WebViewFallback implements CustomTabsHelper.CustomTabFallback {

    private int theme;
    private int toolbarColor;
    private int closeButtonIcon;
    private int toolbarItemColor;

    /**
     * @param context The {@link Context} that wants to open the Uri
     * @param uri     The {@link Uri} to be opened by the fallback
     */
    @Override
    public void openUri(final Context context, final Uri uri) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        intent.putExtra(WebViewActivity.EXTRA_ACTIVITY_THEME, theme);
        intent.putExtra(WebViewActivity.EXTRA_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(WebViewActivity.EXTRA_CLOSE_BUTTON_ICON, closeButtonIcon);
        intent.putExtra(WebViewActivity.EXTRA_TOOLBAR_ITEM_COLOR, toolbarItemColor);
        context.startActivity(intent);
    }

    public WebViewFallback setTheme(@StyleRes int theme) {
        this.theme = theme;
        return this;
    }

    public WebViewFallback setToolbarColor(@ColorInt int toolbarColor) {
        this.toolbarColor = toolbarColor;
        return this;
    }

    public WebViewFallback setCloseButtonIcon(@DrawableRes int closeButtonIcon) {
        this.closeButtonIcon = closeButtonIcon;
        return this;
    }

    public WebViewFallback setToolbarItemColor(@ColorInt int toolbarItemColor) {
        this.toolbarItemColor = toolbarItemColor;
        return this;
    }
}
