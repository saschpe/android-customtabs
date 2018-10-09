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

package com.example.saschpe.customtabs.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.browser.customtabs.CustomTabsIntent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.saschpe.customtabs.R;

import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public final class MainActivity extends AppCompatActivity {
    private static final String GITHUB_PAGE = "https://github.com/saschpe/android-customtabs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGitHubProjectCustomTab();
            }
        });
    }

    /**
     * Start GitHub project custom tab
     * <p>
     * See https://developer.chrome.com/multidevice/android/customtabs
     */
    private void startGitHubProjectCustomTab() {
        // Apply some fancy animation to show off
        CustomTabsIntent customTabsIntent = getDefaultCustomTabsIntentBuilder()
                .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)
                .build();

        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);

        // This is where the magic happens...
        CustomTabsHelper.openCustomTab(
                this, customTabsIntent,
                Uri.parse(GITHUB_PAGE),
                new WebViewFallback());
    }

    /**
     * Apply some sane defaults across a single app.
     * <b>
     * Not strictly necessary but simplifies code when having many different
     * custom tab intents in one app.
     *
     * @return {@link CustomTabsIntent.Builder} with defaults already applied
     */
    private CustomTabsIntent.Builder getDefaultCustomTabsIntentBuilder() {
        Bitmap backArrow = getBitmapFromVectorDrawable(R.drawable.ic_arrow_back_white_24dp);
        return new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(this.getResources().getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .setCloseButtonIcon(backArrow);
    }

    /**
     * Converts a vector asset to a bitmap as required by {@link CustomTabsIntent.Builder#setCloseButtonIcon(Bitmap)}
     *
     * @param drawableId The drawable ID
     * @return Bitmap equivalent
     */
    private Bitmap getBitmapFromVectorDrawable(final @DrawableRes int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(this, drawableId);
        if (drawable == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
