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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;

public class ConstructorUtilTest {

  @Test
  public void testToSignature() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    assertEquals(
        "java.lang.String(char[], int, int)", ConstructorUtil.createSignature(constructor));
  }

  @Test
  public void testGetConstructor() throws Exception {
    Constructor<String> constructor =
        String.class.getConstructor(char[].class, int.class, int.class);
    String result =
        ConstructorUtil.newInstance(constructor, new char[] {'a', 'b', 'c', 'd', 'e'}, 1, 3);
    assertEquals("bcd", result);
  }

  public static class Hoge {
    public Hoge(String name) {}
  }
}
