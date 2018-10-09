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
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public final class WebViewActivity extends AppCompatActivity {
    /**
     * Optional title resource for the actionbar / toolbar.
     */
    public static final String EXTRA_TITLE = WebViewActivity.class.getName() + ".EXTRA_TITLE";

    /**
     * Mandatory file to load and display.
     */
    public static final String EXTRA_URL = WebViewActivity.class.getName() + ".EXTRA_URL";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String url = getIntent().getStringExtra(EXTRA_URL);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
}
