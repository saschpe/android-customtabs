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

package com.example.saschpe.customtabs.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import com.example.saschpe.customtabs.R
import com.example.saschpe.customtabs.databinding.ActivityMainBinding
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Apply some sensible defaults across a single app.
     * Not strictly necessary but simplifies code when having many different
     * custom tab intents in one app.
     *
     * @return [CustomTabsIntent.Builder] with defaults already applied
     */
    private val defaultCustomTabsIntentBuilder: CustomTabsIntent.Builder
        get() {
            val builder = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_ON)
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                        .build(),
                )
                .setShowTitle(true)
            getBitmapFromVectorDrawable(R.drawable.ic_arrow_back_white_24dp)?.let {
                builder.setCloseButtonIcon(it)
            }
            return builder
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { startGitHubProjectCustomTab() }
    }

    /**
     * Start a GitHub project custom tab
     *
     * See https://developer.chrome.com/multidevice/android/customtabs
     */
    private fun startGitHubProjectCustomTab() {
        // Apply some fancy animation to show off
        val customTabsIntent = defaultCustomTabsIntentBuilder
            .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)
            .build()

        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent)

        // This is where the magic happens...
        CustomTabsHelper.openCustomTab(
            this,
            customTabsIntent,
            Uri.parse(GITHUB_PAGE),
            WebViewFallback(),
        )
    }

    /**
     * Converts a vector asset to a bitmap as required by [CustomTabsIntent.Builder.setCloseButtonIcon]
     *
     * @param drawableId The drawable ID
     * @return Bitmap equivalent
     */
    private fun getBitmapFromVectorDrawable(@Suppress("SameParameterValue") @DrawableRes drawableId: Int): Bitmap? {
        val drawable = AppCompatResources.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    companion object {
        private const val GITHUB_PAGE = "https://github.com/saschpe/android-customtabs"
    }
}
