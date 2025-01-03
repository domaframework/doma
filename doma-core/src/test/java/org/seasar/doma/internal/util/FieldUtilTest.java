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
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

public class FieldUtilTest {

  public String aaa;

  String bbb;

  @Test
  public void testIsPublic() throws Exception {
    Field aaa = FieldUtilTest.class.getField("aaa");
    assertTrue(FieldUtil.isPublic(aaa));

    Field bbb = FieldUtilTest.class.getDeclaredField("bbb");
    assertFalse(FieldUtil.isPublic(bbb));
  }
}
