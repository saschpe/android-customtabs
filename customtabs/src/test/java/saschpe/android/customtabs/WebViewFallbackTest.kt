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

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class WebViewFallbackTest {
    @Test
    fun openUri_shouldStartWebViewActivity() {
        // Arrange
        val mockContext = mock(Context::class.java)
        `when`(mockContext.packageName).thenReturn("foo")
        doNothing().`when`(mockContext).startActivity(ArgumentMatchers.any(Intent::class.java))
        val webViewFallback = WebViewFallback()

        // Act
        webViewFallback.openUri(mockContext, Uri.parse(GITHUB_PAGE))

        // Assert
        verify(mockContext).startActivity(ArgumentMatchers.any(Intent::class.java))
    }

    companion object {
        private const val GITHUB_PAGE = "https://github.com/saschpe/android-customtabs/"
    }
}
