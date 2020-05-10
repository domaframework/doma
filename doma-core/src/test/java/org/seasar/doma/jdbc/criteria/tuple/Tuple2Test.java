package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Tuple2Test {

  @Test
  void test() {
    Tuple2<Integer, String> tuple = new Tuple2<>(1, "a");
    assertEquals(new Tuple2<>(1, "a"), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
  }
}
