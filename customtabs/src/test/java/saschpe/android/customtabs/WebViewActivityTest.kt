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

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric

@RunWith(AndroidJUnit4::class)
@Ignore
class WebViewActivityTest {
    private lateinit var activity: WebViewActivity

    @Before
    fun setupActivity() { // Arrange
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), WebViewActivity::class.java)
                .putExtra(WebViewActivity.EXTRA_TITLE, TEST_TITLE)
                .putExtra(WebViewActivity.EXTRA_URL, TEST_URL)
        activity = Robolectric.buildActivity(WebViewActivity::class.java, intent).create()
            .get()
    }

    @Test
    fun onCreate_shouldSetActionBarTitleAndSubTitle() {
        // Act
        val title = activity.actionBar?.title.toString()
        val subtitle = activity.actionBar?.subtitle.toString()

        // Assert
        assertEquals(TEST_TITLE, title)
        assertEquals(TEST_URL, subtitle)
    }

    @Test
    fun clickingHome_shouldFinishActivity() {
        // TODO:
        assertEquals(1, 1)
    }

    companion object {
        private const val TEST_URL = "http://sascha.peilicke.de"
        private const val TEST_TITLE = "Sascha Peilicke"
    }
}