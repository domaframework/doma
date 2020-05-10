package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Tuple3Test {

  @Test
  void test() {
    Tuple3<Integer, String, Boolean> tuple = new Tuple3<>(1, "a", true);
    assertEquals(new Tuple3<>(1, "a", true), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
  }
}
