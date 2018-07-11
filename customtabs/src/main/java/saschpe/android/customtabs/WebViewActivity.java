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

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import saschpe.android.customtabs.utils.Utils;

import static saschpe.android.customtabs.CustomTabsHelper.UNDEFINED_RESOURCE;

public final class WebViewActivity extends AppCompatActivity {
    /**
     * Optional title resource for the actionbar / toolbar.
     */
    public static final String EXTRA_TITLE = WebViewActivity.class.getName() + ".EXTRA_TITLE";

    /**
     * Mandatory file to load and display.
     */
    public static final String EXTRA_URL = WebViewActivity.class.getName() + ".EXTRA_URL";

    /**
     * Optional close button (up navigation) drawable
     * Default is {@link android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material}
     */
    public static final String EXTRA_CLOSE_BUTTON_ICON = WebViewActivity.class.getName() + ".EXTRA_CLOSE_BUTTON_ICON";

    /**
     * Optional close button (up navigation) tint color and title text color
     */
    public static final String EXTRA_TOOLBAR_ITEM_COLOR = WebViewActivity.class.getName() + ".EXTRA_TOOLBAR_ITEM_COLOR";

    /**
     * Optional toolbar background color (colorPrimary in theme)
     */
    public static final String EXTRA_TOOLBAR_COLOR = WebViewActivity.class.getName() + ".EXTRA_TOOLBAR_COLOR";

    /**
     * Optional status bar background color (colorPrimaryDark in theme)
     */
    public static final String EXTRA_TOOLBAR_DARK_COLOR = WebViewActivity.class.getName() + ".EXTRA_TOOLBAR_DARK_COLOR";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.WebViewTheme);
        setContentView(R.layout.activity_webview);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String url = getIntent().getStringExtra(EXTRA_URL);
        int toolbarColor = getIntent().getIntExtra(EXTRA_TOOLBAR_COLOR, UNDEFINED_RESOURCE);
        int toolbarDarkColor = getIntent().getIntExtra(EXTRA_TOOLBAR_DARK_COLOR, UNDEFINED_RESOURCE);
        int closeButtonIcon = getIntent().getIntExtra(EXTRA_CLOSE_BUTTON_ICON, UNDEFINED_RESOURCE);
        final int toolbarItemColor = getIntent().getIntExtra(EXTRA_TOOLBAR_ITEM_COLOR, UNDEFINED_RESOURCE);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (closeButtonIcon != UNDEFINED_RESOURCE || toolbarItemColor != UNDEFINED_RESOURCE) {
                actionBar.setHomeAsUpIndicator(Utils.getDrawable(this,
                        closeButtonIcon == UNDEFINED_RESOURCE ? R.drawable.abc_ic_ab_back_material : closeButtonIcon,
                        toolbarItemColor));
            }

            if (toolbarColor != UNDEFINED_RESOURCE) {
                actionBar.setBackgroundDrawable(new ColorDrawable(toolbarColor));
            }
            if (toolbarDarkColor != UNDEFINED_RESOURCE && Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(toolbarDarkColor);
            }

            if (title != null) {
                actionBar.setTitle(buildTitleSpannable(title, toolbarItemColor));
                actionBar.setSubtitle(buildTitleSpannable(url, toolbarItemColor));
            } else {
                actionBar.setTitle(buildTitleSpannable(url, toolbarItemColor));
            }
        }

        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);

        // No title provided. Use the website's once it's loaded...
        if (title == null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (actionBar != null) {
                        actionBar.setTitle(buildTitleSpannable(view.getTitle(), toolbarItemColor));
                        actionBar.setSubtitle(buildTitleSpannable(url, toolbarItemColor));
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Spannable buildTitleSpannable(String s, int textColor) {
        Spannable text = new SpannableString(s != null ? s : "");
        if (textColor != UNDEFINED_RESOURCE) {
            text.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return text;
    }
}
