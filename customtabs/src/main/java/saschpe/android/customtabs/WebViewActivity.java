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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
     * Optional activity theme
     */
    public static final String EXTRA_ACTIVITY_THEME = WebViewActivity.class.getName() + ".EXTRA_ACTIVITY_THEME";

    /**
     * Optional close button (up navigation) drawable
     * Default is {@link android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material}
     */
    public static final String EXTRA_CLOSE_BUTTON_ICON = WebViewActivity.class.getName() + ".EXTRA_CLOSE_BUTTON_ICON";

    /**
     * Optional close button (up navigation) tint color
     */
    public static final String EXTRA_CLOSE_BUTTON_TINT_COLOR = WebViewActivity.class.getName() + ".EXTRA_CLOSE_BUTTON_TINT_COLOR";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String url = getIntent().getStringExtra(EXTRA_URL);
        int theme = getIntent().getIntExtra(EXTRA_ACTIVITY_THEME, UNDEFINED_RESOURCE);
        int closeButtonIcon = getIntent().getIntExtra(EXTRA_CLOSE_BUTTON_ICON, UNDEFINED_RESOURCE);
        int closeButtonTintColor = getIntent().getIntExtra(EXTRA_CLOSE_BUTTON_TINT_COLOR, UNDEFINED_RESOURCE);

        if (theme != UNDEFINED_RESOURCE) {
            setTheme(theme);
        }
        setContentView(R.layout.activity_webview);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (closeButtonIcon != UNDEFINED_RESOURCE || closeButtonTintColor != UNDEFINED_RESOURCE) {
                actionBar.setHomeAsUpIndicator(buildUpNavigationDrawable(closeButtonIcon, closeButtonTintColor));
            }
            if (title != null) {
                actionBar.setTitle(title);
                actionBar.setSubtitle(url);
            } else {
                actionBar.setTitle(url);
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
                        actionBar.setTitle(view.getTitle());
                        actionBar.setSubtitle(url);
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

    private Drawable buildUpNavigationDrawable(int drawable, int tintColor) {
        Drawable arrowDrawable = getResources().getDrawable(drawable != UNDEFINED_RESOURCE ? drawable : R.drawable.abc_ic_ab_back_material);
        Drawable wrapped = DrawableCompat.wrap(arrowDrawable);

        if (arrowDrawable != null && wrapped != null) {
            // This should avoid tinting all the arrows
            arrowDrawable.mutate();
            if (tintColor != UNDEFINED_RESOURCE) {
                DrawableCompat.setTint(wrapped, tintColor);
            }
        }

        return wrapped;
    }
}
