package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Tuple3Test {

  @Test
  void test() {
    Tuple3<Integer, String, Boolean> tuple3 = new Tuple3<>(1, "a", true);
    assertEquals(new Tuple3<>(1, "a", true), tuple3);
    assertEquals(1, tuple3.first());
    assertEquals("a", tuple3.second());
    assertEquals(true, tuple3.third());
    assertEquals(1, tuple3.component1());
    assertEquals("a", tuple3.component2());
    assertEquals(true, tuple3.component3());
  }
}
