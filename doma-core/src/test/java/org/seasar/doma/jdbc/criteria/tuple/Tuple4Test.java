package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Tuple4Test {

  @Test
  void test() {
    Tuple4<Integer, String, Boolean, Long> tuple = new Tuple4<>(1, "a", true, 1L);
    assertEquals(new Tuple4<>(1, "a", true, 1L), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(1L, tuple.getItem4());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
    assertEquals(tuple.getItem4(), tuple.component4());
  }
}
