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

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class KeepAliveServiceTest {
    // Arrange
    private final Intent mockIntent = mock(Intent.class);

    @Test
    public void onBind_returnsValidBinder() {
        // Act, assert
        assertNotNull(new KeepAliveService().onBind(mockIntent));
    }

    @Test
    public void onBind_returnsSameBinder() {
        // Act, assert
        assertEquals(new KeepAliveService().onBind(mockIntent),
                new KeepAliveService().onBind(mock(Intent.class)));
    }
}
