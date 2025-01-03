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

import org.junit.jupiter.api.Test;

public class CharSequenceUtilTest {

  @Test
  public void testIsEmpty() {
    assertTrue(CharSequenceUtil.isEmpty(null));
    assertTrue(CharSequenceUtil.isEmpty(""));
    assertFalse(CharSequenceUtil.isEmpty(" "));
    assertFalse(CharSequenceUtil.isEmpty(" \t\n\r "));
    assertFalse(CharSequenceUtil.isEmpty("a"));
    assertFalse(CharSequenceUtil.isEmpty(" a "));
  }

  @Test
  public void testIsNotEmpty() {
    assertFalse(CharSequenceUtil.isNotEmpty(null));
    assertFalse(CharSequenceUtil.isNotEmpty(""));
    assertTrue(CharSequenceUtil.isNotEmpty(" "));
    assertTrue(CharSequenceUtil.isNotEmpty(" \t\n\r "));
    assertTrue(CharSequenceUtil.isNotEmpty("a"));
    assertTrue(CharSequenceUtil.isNotEmpty(" a "));
  }

  @Test
  public void testIsBlank() {
    assertTrue(CharSequenceUtil.isBlank(null));
    assertTrue(CharSequenceUtil.isBlank(""));
    assertTrue(CharSequenceUtil.isBlank(" "));
    assertTrue(CharSequenceUtil.isBlank(" \t\n\r "));
    assertFalse(CharSequenceUtil.isBlank("a"));
    assertFalse(CharSequenceUtil.isBlank(" a "));
  }

  @Test
  public void testIsNotBlank() {
    assertFalse(CharSequenceUtil.isNotBlank(null));
    assertFalse(CharSequenceUtil.isNotBlank(""));
    assertFalse(CharSequenceUtil.isNotBlank(" "));
    assertFalse(CharSequenceUtil.isNotBlank(" \t\n\r "));
    assertTrue(CharSequenceUtil.isNotBlank("a"));
    assertTrue(CharSequenceUtil.isNotBlank(" a "));
  }
}
