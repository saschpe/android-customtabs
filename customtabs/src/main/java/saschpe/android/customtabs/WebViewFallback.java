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

    /**
     * Optional activity theme
     */
    private int theme;

    /**
     * Optional up navigation drawable
     * Default is {@link android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material}
     */
    private int upDrawable;

    /**
     * Optional up navigation tint color
     */
    private int upTintColor;

    /**
     * @param context The {@link Context} that wants to open the Uri
     * @param uri     The {@link Uri} to be opened by the fallback
     */
    @Override
    public void openUri(final Context context, final Uri uri) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        intent.putExtra(WebViewActivity.EXTRA_ACTIVITY_THEME, theme);
        intent.putExtra(WebViewActivity.EXTRA_UP_NAVIGATION_DRAWABLE, upDrawable);
        intent.putExtra(WebViewActivity.EXTRA_UP_NAVIGATION_TINT_COLOR, upTintColor);
        context.startActivity(intent);
    }

    public WebViewFallback setTheme(@StyleRes int theme) {
        this.theme = theme;
        return this;
    }

    public WebViewFallback setUpDrawable(@DrawableRes int upDrawable) {
        this.upDrawable = upDrawable;
        return this;
    }

    public WebViewFallback setUpTintColor(@ColorInt int upTintColor) {
        this.upTintColor = upTintColor;
        return this;
    }
}
