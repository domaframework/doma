package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.entity.NamingType;

public class NamingTest {

  @Test
  public void testNONE() throws Exception {
    Naming naming = Naming.NONE;

    assertEquals("hogeFoo", naming.apply(null, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(null, "hogeFoo"));

    assertEquals("HOGE_FOO", naming.apply(NamingType.SNAKE_UPPER_CASE, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_UPPER_CASE, "HOGE_FOO"));
  }

  @Test
  public void testSNAKE_UPPER_CASE() throws Exception {
    Naming naming = Naming.SNAKE_UPPER_CASE;

    assertEquals("HOGE_FOO", naming.apply(null, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(null, "HOGE_FOO"));

    assertEquals("hoge_foo", naming.apply(NamingType.SNAKE_LOWER_CASE, "hogeFoo"));
    assertEquals("hogeFoo", naming.revert(NamingType.SNAKE_LOWER_CASE, "hoge_foo"));
  }
}
