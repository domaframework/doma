package org.seasar.doma.jdbc.criteria.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class Tuple7Test {

  @Test
  void test() {
    Tuple7<Integer, String, Boolean, Long, BigDecimal, Integer, String> tuple =
        new Tuple7<>(1, "a", true, 1L, BigDecimal.ONE, 2, "b");
    assertEquals(new Tuple7<>(1, "a", true, 1L, BigDecimal.ONE, 2, "b"), tuple);
    assertEquals(1, tuple.getItem1());
    assertEquals("a", tuple.getItem2());
    assertEquals(true, tuple.getItem3());
    assertEquals(1L, tuple.getItem4());
    assertEquals(BigDecimal.ONE, tuple.getItem5());
    assertEquals(2, tuple.getItem6());
    assertEquals("b", tuple.getItem7());
    assertEquals(tuple.getItem1(), tuple.component1());
    assertEquals(tuple.getItem2(), tuple.component2());
    assertEquals(tuple.getItem3(), tuple.component3());
    assertEquals(tuple.getItem4(), tuple.component4());
    assertEquals(tuple.getItem5(), tuple.component5());
    assertEquals(tuple.getItem6(), tuple.component6());
    assertEquals(tuple.getItem7(), tuple.component7());
  }
}
