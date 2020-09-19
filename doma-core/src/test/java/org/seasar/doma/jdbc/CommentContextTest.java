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
