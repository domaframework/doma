/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class IOUtilTest {

  @Test
  public void test() {
    IOUtil.close(
        () -> {
          throw new IOException();
        });
  }

  @Test
  public void testEndWith_true() {
    File file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  @Test
  public void testEndWith_false() {
    File file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}
