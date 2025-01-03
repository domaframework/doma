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
package org.seasar.doma.internal.jdbc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DatabaseObjectUtilTest {

  @Test
  public void testGetQualifiedName() {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", "bbb", "ccc");
    assertEquals("[aaa].[bbb].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_catalogIsNull() {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, "bbb", "ccc");
    assertEquals("[bbb].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_schemaIsNull() {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", null, "ccc");
    assertEquals("[aaa].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_catalogAndSchemaIsNull() {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, null, "ccc");
    assertEquals("[ccc]", name);
  }
}
