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
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommentContextTest {

  @Test
  void getMessage() {
    CommentContext context = new CommentContext("class", "method", new MockConfig(), null, "hello");
    assertEquals("hello", context.getMessage().get());
  }

  @Test
  void getMessage_empty() {
    CommentContext context = new CommentContext("class", "method", new MockConfig(), null, null);
    assertFalse(context.getMessage().isPresent());
  }
}
