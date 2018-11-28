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

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class WebViewFallbackTest {
    private static final String GITHUB_PAGE = "https://github.com/saschpe/android-customtabs/";

    @Test
    public void openUri_shouldStartWebViewActivity() {
        // Arrange
        Context mockContext = mock(Context.class);
        when(mockContext.getPackageName()).thenReturn("foo");
        doNothing().when(mockContext).startActivity(any(Intent.class));

        WebViewFallback webViewFallback = new WebViewFallback();
        /*Intent expectedIntent = new Intent(mockContext, WebViewActivity.class)
                .putExtra(WebViewActivity.EXTRA_URL, GITHUB_PAGE);*/

        // Act
        webViewFallback.openUri(mockContext, Uri.parse(GITHUB_PAGE));

        // Assert
        verify(mockContext).startActivity(any(Intent.class));
    }
}
