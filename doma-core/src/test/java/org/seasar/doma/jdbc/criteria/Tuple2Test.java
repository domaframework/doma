package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Tuple2Test {

  @Test
  void test() {
    Tuple2<Integer, String> tuple2 = new Tuple2<>(1, "a");
    assertEquals(new Tuple2<>(1, "a"), tuple2);
    assertEquals(1, tuple2.first());
    assertEquals("a", tuple2.second());
    assertEquals(1, tuple2.component1());
    assertEquals("a", tuple2.component2());
  }
}
