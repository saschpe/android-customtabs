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

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.saschpe.customtabs.R;

import saschpe.android.customtabs.CustomTabsHelper;

import static saschpe.android.customtabs.CustomTabsHelper.UNDEFINED_RESOURCE;

public final class MainActivity extends AppCompatActivity {
    private static final String GITHUB_PAGE = "https://github.com/saschpe/android-customtabs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabCustom = findViewById(R.id.fab_custom);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGitHubProjectCustomTab(false);
            }
        });
        fabCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGitHubProjectCustomTab(true);
            }
        });
    }

    /**
     * Start GitHub project custom tab
     * <p>
     * See https://developer.chrome.com/multidevice/android/customtabs
     */
    private void startGitHubProjectCustomTab(boolean customActionBar) {
        CustomTabsHelper.Builder customTabsHelperBuilder = new CustomTabsHelper.Builder(this, Uri.parse(GITHUB_PAGE))
                .setTheme(customActionBar ? R.style.AppThemeCustom : UNDEFINED_RESOURCE)
                .setToolbarColor(ContextCompat.getColor(this, customActionBar ? R.color.colorPrimaryCustom : R.color.colorPrimary))
                .setCloseButtonIcon(R.drawable.ic_arrow_back_white_24dp)
                .setToolbarItemColor(customActionBar ? ContextCompat.getColor(this, R.color.colorTitleTintCustom) : UNDEFINED_RESOURCE)
                .setAddKeepAliveExtra(true);

        // Get and apply some fancy animation to show off
        customTabsHelperBuilder.getCustomTabsIntentBuilder()
                .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

        customTabsHelperBuilder.open();
    }
}
