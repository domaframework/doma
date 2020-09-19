package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class CallerCommenterTest {

  private final CallerCommenter commenter = new CallerCommenter();

  @Test
  public void testComment() throws Exception {
    CommentContext context = new CommentContext("class", "method", new MockConfig(), null, null);
    String actual = commenter.comment("select * from emp", context);
    assertEquals("/** class.method */" + System.lineSeparator() + "select * from emp", actual);
  }
}
