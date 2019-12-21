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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class KeepAliveServiceTest {
    // Arrange
    private val mockIntent = Mockito.mock(Intent::class.java)

    @Test
    fun onBind_returnsValidBinder() { // Act, assert
        assertNotNull(KeepAliveService().onBind(mockIntent))
    }

    @Test
    fun onBind_returnsSameBinder() { // Act, assert
        assertEquals(
            KeepAliveService().onBind(mockIntent),
            KeepAliveService().onBind(Mockito.mock(Intent::class.java))
        )
    }
}