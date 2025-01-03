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
package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.entity.NamingType;

public class NamingTest {

  @Test
  public void testNONE() {
    Naming naming = Naming.NONE;

    assertEquals("hogeFoo", naming.apply(null, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(null, "hogeFoo"));

    assertEquals("HOGE_FOO", naming.apply(NamingType.SNAKE_UPPER_CASE, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_UPPER_CASE, "HOGE_FOO"));
  }

  @Test
  public void testSNAKE_UPPER_CASE() {
    Naming naming = Naming.SNAKE_UPPER_CASE;

    assertEquals("HOGE_FOO", naming.apply(null, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(null, "HOGE_FOO"));

    assertEquals("hoge_foo", naming.apply(NamingType.SNAKE_LOWER_CASE, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_LOWER_CASE, "hoge_foo"));
  }
}
