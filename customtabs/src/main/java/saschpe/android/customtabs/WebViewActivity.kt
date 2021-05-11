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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity(R.layout.activity_webview) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val url = intent.getStringExtra(EXTRA_URL)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            if (title != null) {
                actionBar.title = title
                actionBar.subtitle = url
            } else {
                actionBar.title = url
            }
        }

        val webView = findViewById<WebView>(R.id.web_view)
        url?.let { webView.loadUrl(it) }
        webView.settings.javaScriptEnabled = true

        // No title provided. Use the website's once it's loaded...
        if (title == null) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    if (actionBar != null) {
                        actionBar.title = view.title
                        actionBar.subtitle = url
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Optional title resource for the actionbar / toolbar.
         */
        val EXTRA_TITLE = "${WebViewActivity::class.java.name}.EXTRA_TITLE"

        /**
         * Mandatory file to load and display.
         */
        val EXTRA_URL = "${WebViewActivity::class.java.name}.EXTRA_URL"
    }
}
